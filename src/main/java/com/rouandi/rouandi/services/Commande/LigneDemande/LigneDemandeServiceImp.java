package com.rouandi.rouandi.services.Commande.LigneDemande;

import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.rouandi.rouandi.DTOs.DechargeDTO.DechargeDTO;
import com.rouandi.rouandi.DTOs.DestinataireDTO.DestinataireMapper;
import com.rouandi.rouandi.DTOs.LigneDemandeDTO.LigneDemandeRequestDTO;
import com.rouandi.rouandi.entities.*;
import com.rouandi.rouandi.repositories.*;
import com.rouandi.rouandi.services.Commande.Demande.DemandeService;
import com.rouandi.rouandi.services.Commande.User.EmailService;
import com.rouandi.rouandi.services.Commande.User.UserServiceImp;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.lowagie.text.Document;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Time;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.rouandi.rouandi.entities.EtatLigne.*;


@Transactional
@Service
public class LigneDemandeServiceImp implements LigneDemandeService {

    private String uploadDir;
    private String decharge;
    private LigneDemandeRepository ligneDemandeRepository;
    private DestinataireRepository destinataireRepository;
    private DemandeService demandeService;
    private MaterielRepository materielRepository;
    private DemandeRepository demandeRepository;
    private UserServiceImp userServiceImp;
    private EmailService emailService;

    public LigneDemandeServiceImp(@Value("${upload.dir.uploads}") String uploadDir, LigneDemandeRepository ligneDemandeRepository,
                                  DestinataireRepository destinataireRepository, DemandeService demandeService,
                                  MaterielRepository materielRepository, DemandeRepository demandeRepository,
                                  UserServiceImp userServiceImp, EmailService emailService, @Value("${upload.dir.decharges}") String decharge) {
        this.uploadDir = uploadDir;
        this.ligneDemandeRepository = ligneDemandeRepository;
        this.destinataireRepository = destinataireRepository;
        this.demandeService = demandeService;
        this.materielRepository = materielRepository;
        this.demandeRepository = demandeRepository;
        this.userServiceImp = userServiceImp;
        this.emailService = emailService;
        this.decharge = decharge;
    }

    @Transactional
    @Override
    public void ajouterLignesDemande(List<LigneDemandeRequestDTO> lignes, List<MultipartFile> fichiers) throws IOException {
    /*    if (lignes == null || lignes.isEmpty()) {
            throw new IllegalArgumentException("La liste de lignes ne doit pas être vide");
        }*/

        String dossierBase = uploadDir + "/" + YearMonth.now().toString();
        Path dossierPath = Paths.get(dossierBase);
        if (!Files.exists(dossierPath)) {
            Files.createDirectories(dossierPath);
        }
        Demande demande = new Demande();
        demande.setEtat(Etat.ENVOYE);
        demande.setDate(new Date());
        demande.setTime(new Time(System.currentTimeMillis()));
        demande.setCreateur(userServiceImp.getCurrentUser());
        demande.setDuree(0L);
        emailService.sendEmail(
                demande.getCreateur().getEmail(),
                "elbouazizi.fat.fst@uhp.ac.ma",
                "Ajout d'une nouvelle demande ",
                " Nouvelle demande – Visitez la plateforme pour le suivi. "
        );
        demandeRepository.save(demande);

        for (int i = 0; i < lignes.size(); i++) {
            LigneDemandeRequestDTO dto = lignes.get(i);
            Materiel materiel = materielRepository.findById(dto.getMaterielId())
                    .orElseThrow(() -> new IllegalArgumentException("Matériel non trouvé : " + dto.getNomMateriel()));
            Destinataire destinataire = destinataireRepository
                    .findDestinataireByEmail(dto.getEmailDestinataire())
                    .orElseGet(() -> destinataireRepository.save(DestinataireMapper.toEntityFromDTO(dto)));

            LigneDemande ligne = new LigneDemande();
            ligne.setDemande(demande);
            ligne.setMateriel(materiel);
            ligne.setDestinataire(destinataire);
            ligne.setQuantite(dto.getQuantite());
            ligne.setDescription(dto.getDescription());
            ligne.setEtat(ENVOYE);
            ligne.setDureeLogistique(0L);
            ligne.setDureeSi(0L);
     /*       Optional<LigneDemande> existante = ligneDemandeRepository
                    .findByMaterielAndDestinataireAndQuantiteAndDescription(
                            materiel, destinataire, dto.getQuantite(), dto.getDescription());*/

            /*if (existante.isPresent()) {
                throw new IllegalArgumentException("Doublon détecté : ligne avec " + dto.getNomMateriel() + " déjà existante.");
            }*/
            Integer fileIndex = dto.getFileIndex();

            if (fichiers != null && fileIndex != null && fileIndex >= 0 && fichiers.size() > i) {

                MultipartFile fichier = fichiers.get(fileIndex);
                if (fichier != null && !fichier.isEmpty()) {

                    String extension = getExtension(fichier.getOriginalFilename());
                    String nomFichier = UUID.randomUUID().toString() + "." + extension;

                    Path cheminFichier = dossierPath.resolve(nomFichier);
                    Files.copy(fichier.getInputStream(), cheminFichier);

                    ligne.setFicheTechnique(YearMonth.now().toString() + "/" + nomFichier);

                }
            }

            ligneDemandeRepository.save(ligne);
        }

    }

    private String getExtension(String filename) {
        if (filename == null) return "";
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? "" : filename.substring(dotIndex + 1);
    }

    private Path getFichierPath(String cheminRelatif) {
        if (cheminRelatif == null || cheminRelatif.isBlank()) return null;
        return Paths.get(uploadDir).resolve(cheminRelatif);
    }

    @Override
    public void supprimerLigneDemande(Long id) {
        if (ligneDemandeRepository.existsLigneDemandeById(id))
            ligneDemandeRepository.deleteById(id);
        else
            System.out.println("la ligne n'existe pas");

    }

    @Override
    public void modifierLigneDemande(Long idLigne, LigneDemandeRequestDTO dto, MultipartFile nouvelleFiche) throws IOException {
        LigneDemande ligne = ligneDemandeRepository.findById(idLigne)
                .orElseThrow(() -> new RuntimeException("LigneDemande introuvable"));

        if (dto.getQuantite() > 0) {
            ligne.setQuantite(dto.getQuantite());
        }
        if (dto.getMaterielId() != null) {
            Materiel materiel = materielRepository.findById(dto.getMaterielId())
                    .orElseThrow(() -> new RuntimeException("Matériel non trouvé"));
            ligne.setMateriel(materiel);
        }

       /* if (dto.getNomMateriel() != null && !dto.getNomMateriel().isBlank()) {
            ligne.getMateriel().setNomMateriel(dto.getNomMateriel());
        }

        if (dto.getTypeMateriel() != null && !dto.getTypeMateriel().isBlank()) {
            ligne.getMateriel().setTypeMateriel(dto.getTypeMateriel());
        }
*/
        if (dto.getDescription() != null && !dto.getDescription().isBlank()) {
            ligne.setDescription(dto.getDescription());
        }
        Destinataire dest = ligne.getDestinataire();
        if (dto.getNomDestinataire() != null && !dto.getNomDestinataire().isBlank()) {
            dest.setNom(dto.getNomDestinataire());
        }

        if (dto.getPrenomDestinataire() != null && !dto.getPrenomDestinataire().isBlank()) {
            dest.setPrenom(dto.getPrenomDestinataire());
        }

        if (dto.getEmailDestinataire() != null && !dto.getEmailDestinataire().isBlank()) {
            dest.setEmail(dto.getEmailDestinataire());
        }

        if (dto.getProjetDestinataire() != null && !dto.getProjetDestinataire().isBlank()) {
            dest.setProjet(dto.getProjetDestinataire());
        }

        if (dto.getTypeDestinataire() != null && !dto.getTypeDestinataire().isBlank()) {
            dest.setType(Type.valueOf(dto.getTypeDestinataire()));
        }


        if (nouvelleFiche != null && !nouvelleFiche.isEmpty()) {


            if (ligne.getFicheTechnique() != null) {
                Path ancienFichier = Paths.get(uploadDir, ligne.getFicheTechnique());
                Files.deleteIfExists(ancienFichier);
            }

            String extension = getExtension(nouvelleFiche.getOriginalFilename());
            String nomFichier = UUID.randomUUID().toString() + "." + extension;
            String dossierBase = uploadDir + "/" + YearMonth.now();
            Path dossierPath = Paths.get(dossierBase);
            if (!Files.exists(dossierPath)) {
                Files.createDirectories(dossierPath);
            }
            Path cheminFichier = dossierPath.resolve(nomFichier);
            Files.copy(nouvelleFiche.getInputStream(), cheminFichier);


            ligne.setFicheTechnique(YearMonth.now() + "/" + nomFichier);
        }

        ligneDemandeRepository.save(ligne);
    }


    public byte[] generateDecharge(DechargeDTO dto) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            Font underlineFont = new Font(Font.HELVETICA, 14, Font.UNDERLINE);
            Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 17, Font.UNDERLINE);
            Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 14);
            Font font = new Font(Font.HELVETICA, 15, Font.BOLDITALIC);
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Font normalFont = new Font(Font.HELVETICA, 14, Font.NORMAL);
            InputStream is = getClass().getResourceAsStream("/images/lr.png");
            if (is == null) {
                throw new RuntimeException("Logo introuvable !");
            }
            byte[] logoBytes = is.readAllBytes();
            Image img = Image.getInstance(logoBytes);
            img.scaleToFit(180, 180);
            document.add(img);

            document.addTitle("Décharge" + dto.getNomDestinataire() + dto.getPrenomDestinataire());
            Paragraph p0 = new Paragraph("Le " + dto.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                    ", à " + dto.getLieu().toUpperCase(), fontNormal);
            p0.setAlignment(Paragraph.ALIGN_RIGHT);
            document.add(p0);
            Paragraph p1 = new Paragraph("\nDécharge de matériel informatique\n\n", fontTitle);
            p1.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(p1);

            Paragraph p2 = new Paragraph(dto.getNomDestinataire() + " " + dto.getPrenomDestinataire(), font);
            p2.setAlignment(Paragraph.ALIGN_RIGHT);
            document.add(p2);

            Chunk bold1 = new Chunk(" MATERIEL INFORMATIQUE\n", boldFont);
            Chunk normal1 = new Chunk(" \n  Objet : Décharge de", normalFont);
            Paragraph p4 = new Paragraph();

            p4.add(normal1);
            p4.add(bold1);
            document.add(p4);

            document.add(new Paragraph("\n" + dto.getNomDestinataire() + " " + dto.getPrenomDestinataire() + ",\n", fontNormal));

            Chunk c1 = new Chunk(" « ROUANDI SARL AU »", boldFont);
            Chunk c2 = new Chunk(" Service INFORMATIQUE", boldFont);

            Chunk c3 = new Chunk(" \n Afin de mener à bien votre mission, La société", fontNormal);
            Chunk c4 = new Chunk(" vous confie, pendant toute la durée de votre travail au sein de ses établissements," + dto.getNomMateriel() + " " + dto.getTypeMateriel() + ",", fontNormal);
            Paragraph p5 = new Paragraph();
            p5.add(c3);
            p5.add(c1);
            p5.add(c4);
            document.add(p5);


            document.add(new Paragraph("Les caractéristiques de ce matériel sont les suivants :", fontNormal));
            for (String ligne : dto.getCaracteristiques()) {
                document.add(new Paragraph("- " + ligne, boldFont));
            }
            Chunk c5 = new Chunk("\n Ce matériel demeure toujours la propriété de La société", fontNormal);
            Chunk c6 = new Chunk("vous êtes responsable de maintenir cet outil de travail dans un parfait état de \n" +
                    "fonctionnement. Si vous rencontrerez des difficultés, veuillez en avertir le", fontNormal);
            Chunk c7 = new Chunk("vous êtes responsable de maintenir cet outil de travail dans un parfait état de \n" +
                    "fonctionnement. Si vous rencontrerez des difficultés, veuillez en avertir le", fontNormal);

            Paragraph p6 = new Paragraph();
            p6.add(c5);
            p6.add(c1);
            p6.add(c6);
            p6.add(c2);
            document.add(p6);
            Chunk c8 = new Chunk("\n Nous vous rappelons que l’ordinateur portable susvisé mis à votre disposition ce \n" +
                    "jour ne peut en aucun cas être utilisé pour des raisons personnelles. Son usage \n" +
                    "doit en effet rester strictement professionnel, toutefois en cas de panne du \n" +
                    "matériel vous devez prendre l’autorisation du responsable IT pour que vous \n" +
                    "puissiez le réparer.  \n" +
                    "Par ce présent document, vous vous engagez à restituer le matériel informatique \n" +
                    "confié en bon état et vous ne devez pas le formater et le laisser en son état lors de \n" +
                    "la rupture du contrat avec La société", fontNormal);
            Chunk c9 = new Chunk(" et ce, quel que soit le " +
                    "motif de cette rupture (licenciement, démission . . .)  ", fontNormal);
            Paragraph p7 = new Paragraph();
            p7.add(c8);
            p7.add(c1);
            p7.add(c9);
            document.add(p7);
            document.add(new Paragraph("       Veuillez Signer cette décharge, précédé de la mention \" lu et approuvé \". \n" +
                    "                         Fait en deux exemplaires. ", fontNormal));
            Paragraph p3 = new Paragraph("\n\n signature", fontNormal);
            p3.setAlignment(Paragraph.ALIGN_RIGHT);
            document.add(p3);

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du PDF", e);
        }
    }


    public DechargeDTO construireDTODepuisLigne(Long idLigne, String[] caract, String ville) {
        LigneDemande ligne = ligneDemandeRepository.findById(idLigne)
                .orElseThrow(() -> new RuntimeException("Ligne introuvable"));

        Destinataire dest = ligne.getDestinataire();
        Materiel mat = ligne.getMateriel();
/*
        String[] caract = {
                mat.getNomMateriel() + " " + mat.getTypeMateriel() + " - " + ligne.getQuantite() + " unité(s)",
                "Description : " + ligne.getDescription()
        };*/

        return new DechargeDTO(
                dest.getNom(),
                dest.getPrenom(),
                new Date(),
                ville,
                caract,
                mat.getNomMateriel(),
                mat.getTypeMateriel()
        );
    }

    @Override
    public void emailAchat(Long idLigne) {
        LigneDemande ligne = ligneDemandeRepository.findById(idLigne)
                .orElseThrow(() -> new RuntimeException("Ligne introuvable"));

        Materiel m = ligne.getMateriel();
        Destinataire d = ligne.getDestinataire();
        Path fichierPath = getFichierPath(ligne.getFicheTechnique());

        File fichier = null;
        if (fichierPath != null && Files.exists(fichierPath)) {
            fichier = fichierPath.toFile();
        }
        emailService.email("fatimaezzahraelbouazizi@gmail.com", "elbouazizi.fat.fst@uhp.ac.ma", "Demande d'Achat",
                " Bonjour,\n" +
                        "\n" +
                        "Je souhaite soumettre une demande d’achat pour le matériel suivant :\n" +
                        "\n" +
                        "Matériel : " + ligne.getMateriel().getNomMateriel() + " " + ligne.getMateriel().getTypeMateriel() +
                        "\n" +
                        "Quantité : " + ligne.getQuantite() +
                        "\n" +
                        "Description : " + ligne.getDescription() +
                        "\n" +
                        "Type : " + ligne.getDestinataire().getType() +
                        "\n" +
                        "Projet : " + ligne.getDestinataire().getProjet(), fichier);
    }

    @Override
    public void ajouterDecharge(MultipartFile fichier, Long idLigne) throws IOException {

        LigneDemande ligne = ligneDemandeRepository.findById(idLigne)
                .orElseThrow(() -> new RuntimeException("Ligne introuvable"));

        String dossierBase = decharge + "/" + YearMonth.now().toString();
        Path dossierPath = Paths.get(dossierBase);
        if (!Files.exists(dossierPath)) {
            Files.createDirectories(dossierPath);
        }
        if (fichier != null && !fichier.isEmpty()) {
            String extension = getExtension(fichier.getOriginalFilename());
            String nomFichier = UUID.randomUUID().toString() + "." + extension;

            Path cheminFichier = dossierPath.resolve(nomFichier);
            Files.copy(fichier.getInputStream(), cheminFichier);

            ligne.setDecharge(YearMonth.now().toString() + "/" + nomFichier);
        }

    }

    @Override
    public void update(Long id, EtatLigne etat) {
        LigneDemande ligne = ligneDemandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ligne de demande introuvable"));

        ligne.setEtat(etat);

        ligneDemandeRepository.updateEtatById(id, etat);
        String emailCreateur = ligne.getDemande().getCreateur().getEmail();
        if (emailCreateur != null && !emailCreateur.isEmpty()) {
            emailService.sendEmail(
                    "elbouazizi.fat.fst@uhp.ac.ma",
                    emailCreateur,
                    "Changement d'état de votre Ligne de demande ",
                    "L'état actuel de votre demande est : " + etat
            );
        }
    }

    @Override
    public void ajouterLivraision(Long id, TypeLivraison type) {
        LigneDemande ligne = ligneDemandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ligne de demande introuvable"));
        String emailCreateur = ligne.getDemande().getCreateur().getEmail();
        if (type == TypeLivraison.LOGISTIQUE) {
            if (emailCreateur != null && !emailCreateur.isEmpty()) {
                emailService.sendEmail(
                        "elbouazizi.fat.fst@uhp.ac.ma",
                        emailCreateur,
                        "TYPE DE LIVRAISON ",
                        "Je vous écris concernant la livraison de \n" + ligne.getMateriel().getNomMateriel() + ligne.getMateriel().getTypeMateriel()+
                                "Livraison via logistique : prise en charge par un prestataire logistique "
                );
                emailService.sendEmail(
                        "elbouazizi.fat.fst@uhp.ac.ma",
                        "fatimaezzahraelbouazizi@gmail.com",
                        "LIVRAISON ",
                        " Livraison via LOGISTIQUE  "
                );
            }

        }
        if (type == TypeLivraison.LOCAL) {
            if (emailCreateur != null && !emailCreateur.isEmpty()) {
                emailService.sendEmail(
                        "elbouazizi.fat.fst@uhp.ac.ma",
                        emailCreateur,
                        "TYPE DE LIVRAISION ",
                        "Je vous écris concernant la livraison de \n" + ligne.getMateriel().getNomMateriel() + ligne.getMateriel().getTypeMateriel()+
                                " Livraison locale "
                );
            }

        }
        ligne.setTypeLivraison(type);
        ligneDemandeRepository.save(ligne);
    }

    @Override
    public void actualiserDuree() {
        List<LigneDemande> lignes = ligneDemandeRepository.findAll();

        Date today = new Date();

        for (LigneDemande l : lignes) {
            EtatLigne etat = l.getEtat();

            if (etat == EtatLigne.ENVOYE || etat == EtatLigne.ENCOURS) {
                long diffInMillies = today.getTime() - l.getDemande().getDate().getTime();
                long diffJours = diffInMillies / (24 * 60 * 60 * 1000);
                l.setDureeSi(diffJours);
                ligneDemandeRepository.save(l);
            }
            if (etat == EtatLigne.A_LIVRER) {
                long diffInMillies = today.getTime() - l.getDemande().getDate().getTime();
                long diffJours = diffInMillies / (24 * 60 * 60 * 1000);
                l.setDureeLogistique(diffJours);
                ligneDemandeRepository.save(l);
            }
        }

    }
    @Transactional
    public void incrementerDureeParEtat() {
        List<LigneDemande> lignes = ligneDemandeRepository.findAll();
        for (LigneDemande l : lignes) {
            EtatLigne etat = l.getEtat();

            if (etat == EtatLigne.ENVOYE || etat == EtatLigne.ENCOURS) {
                // incrémente dureeSi
                Long current = l.getDureeSi() != null ? l.getDureeSi() : 0L;
                l.setDureeSi(current + 1);
            } else if (etat == EtatLigne.A_LIVRER) {
                // incrémente dureeLogistique
                Long current = l.getDureeLogistique() != null ? l.getDureeLogistique() : 0L;
                l.setDureeLogistique(current + 1);
            }
            ligneDemandeRepository.save(l);
        }
    }

}



