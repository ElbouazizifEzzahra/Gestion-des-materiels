import {Component, inject, signal} from '@angular/core';
import {MaterielService} from '../services/MaterielService';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {Materiel,EditForm} from '../models/Materiel';
import {LayoutComponent} from '../core/layout/layout/layout.component';


@Component({
  selector: 'app-materiels',
  imports: [CommonModule, ReactiveFormsModule, FormsModule, LayoutComponent],
  standalone: true,
  templateUrl: './materiels.component.html',
  styleUrl: './materiels.component.css'
})
export class MaterielsComponent {

  showForm = false;
  materiels: any[] = [];
  isLoading = true;
 materielForm!: FormGroup;

  private materiel = inject(MaterielService);

  selectedMateriel = signal<Materiel | null>(null);
  showEditModal = signal<boolean>(false);
  editForm = signal<EditForm>({
    nom: '',
    type: ''
  });
  constructor(
    private fb: FormBuilder
  ) {
    this.materielForm = this.fb.group({
      nomMateriel: ['', Validators.required],
      typeMateriel: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.materielForm.valid) {
      this.isLoading = true;
      this.materiel.addMateriel(this.materielForm.value).subscribe({
        next: (response) => {
          this.materiels.push(response);
          this.materielForm.reset();
          this.showForm = false;
          this.isLoading = false;
          this.showSuccess('Matériel ajouté !');

        },
        error: (err) => {
          console.error('Erreur:', err);
          this.isLoading = false;
        }
      });
    }
  }


  deleteMateriel(id: number) {
    if (confirm('Êtes-vous sûr ?')) {
      this.materiel.deleteMateriel(id).subscribe({
        next: () => this.showSuccess('Supprimé avec succès'),
        error: () => this.showError('Échec de la suppression')
      });

    }
  }
  openAddForm(): void {
    this.showForm = true;
  }
  cancelForm(): void {
    this.materielForm.reset();
    this.showForm = false;
  }
  ngOnInit(): void {
    this.materiel.getMateriels().subscribe({
      next: (data) => {

        this.materiels = Array.isArray(data) ? data : [];
        this.isLoading = false;
      },
      error: (err) => {
        console.error({err: 'Erreur:'}, err);
        this.isLoading = false;
      }
    });
  }
  openEditModal(materiel: Materiel): void {
    this.selectedMateriel.set(materiel);
    this.editForm.set({
      nom: materiel.nomMateriel as string,
      type: materiel.typeMateriel as string
    });
    this.showEditModal.set(true);
  }

  updateMateriel(): void {
    const id = this.selectedMateriel()?.idMateriel;
    if (!id) return;

    const form = this.editForm();
    this.materiel.updateMateriel(id, form.nom, form.type).subscribe({
      next: (updatedMateriel) => {

        const index = this.materiels.findIndex(m => m.idMateriel === id);
        if (index !== -1) {
          this.materiels[index] = {
            ...this.materiels[index],
            nomMateriel: form.nom,
            typeMateriel: form.type
          };
        }
        this.showEditModal.set(false);

      },

    });
  }


  searchTerm: string = '';
  filterMateriels() {
      this.materiel.filterMateriels(this.searchTerm).subscribe({
        next: (materiels) => {
          this.materiels = materiels;
        },
        error: (error) => {
          console.error('Erreur lors du filtrage:', error);
        }
      });
    }
  showSuccess(message: string): void {
    this.showNotification(message, 'bg-green-500');
  }

  // Méthode pour les erreurs
  showError(message: string): void {
    this.showNotification(message, 'bg-red-500');
  }

  // Méthode générique
  private showNotification(message: string, bgClass: string): void {
    const notification = document.createElement('div');
    notification.innerHTML = `
      <div class="fixed justify-center top-4 right-4 p-4 rounded-md text-white ${bgClass} shadow-lg">
        ${message}
      </div>
    `;
    document.body.appendChild(notification);

    setTimeout(() => notification.remove(), 3000);
  }

}
