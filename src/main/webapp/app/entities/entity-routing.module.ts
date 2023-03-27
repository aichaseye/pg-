import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'etablissement',
        data: { pageTitle: 'Etablissements' },
        loadChildren: () => import('./etablissement/etablissement.module').then(m => m.EtablissementModule),
      },
      {
        path: 'enseignant',
        data: { pageTitle: 'Enseignants' },
        loadChildren: () => import('./enseignant/enseignant.module').then(m => m.EnseignantModule),
      },
      {
        path: 'apprenant',
        data: { pageTitle: 'Apprenants' },
        loadChildren: () => import('./apprenant/apprenant.module').then(m => m.ApprenantModule),
      },
      {
        path: 'demande',
        data: { pageTitle: 'Demandes' },
        loadChildren: () => import('./demande/demande.module').then(m => m.DemandeModule),
      },
      {
        path: 'matiere',
        data: { pageTitle: 'Matieres' },
        loadChildren: () => import('./matiere/matiere.module').then(m => m.MatiereModule),
      },
      {
        path: 'chef-etablissement',
        data: { pageTitle: 'ChefEtablissements' },
        loadChildren: () => import('./chef-etablissement/chef-etablissement.module').then(m => m.ChefEtablissementModule),
      },
      {
        path: 'comptable-matiere',
        data: { pageTitle: 'ComptableMatieres' },
        loadChildren: () => import('./comptable-matiere/comptable-matiere.module').then(m => m.ComptableMatiereModule),
      },
      {
        path: 'referant',
        data: { pageTitle: 'Referants' },
        loadChildren: () => import('./referant/referant.module').then(m => m.ReferantModule),
      },
      {
        path: 'bfpa',
        data: { pageTitle: 'BFPAS' },
        loadChildren: () => import('./bfpa/bfpa.module').then(m => m.BFPAModule),
      },
      {
        path: 'file',
        data: { pageTitle: 'Files' },
        loadChildren: () => import('./file/file.module').then(m => m.FileModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
