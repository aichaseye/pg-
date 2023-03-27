import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDemande, Demande } from '../demande.model';
import { DemandeService } from '../service/demande.service';
import { IEnseignant } from 'app/entities/enseignant/enseignant.model';
import { EnseignantService } from 'app/entities/enseignant/service/enseignant.service';
import { IApprenant } from 'app/entities/apprenant/apprenant.model';
import { ApprenantService } from 'app/entities/apprenant/service/apprenant.service';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { EtablissementService } from 'app/entities/etablissement/service/etablissement.service';
import { Motif } from 'app/entities/enumerations/motif.model';
import { TypeDemandeur } from 'app/entities/enumerations/type-demandeur.model';
import { NomEtab } from 'app/entities/enumerations/nom-etab.model';

@Component({
  selector: 'jhi-demande-update',
  templateUrl: './demande-update.component.html',
})
export class DemandeUpdateComponent implements OnInit {
  isSaving = false;
  motifValues = Object.keys(Motif);
  typeDemandeurValues = Object.keys(TypeDemandeur);
  nomEtabValues = Object.keys(NomEtab);

  enseignantsSharedCollection: IEnseignant[] = [];
  apprenantsSharedCollection: IApprenant[] = [];
  etablissementsSharedCollection: IEtablissement[] = [];

  editForm = this.fb.group({
    id: [],
    motif: [null, [Validators.required]],
    typeDemandeur: [null, [Validators.required]],
    nom: [null, [Validators.required]],
    prenom: [null, [Validators.required]],
    nomEtab: [],
    autreNomEtab: [],
    email: [null, [Validators.required]],
    enseignant: [],
    apprenant: [],
    etablissement: [],
  });

  constructor(
    protected demandeService: DemandeService,
    protected enseignantService: EnseignantService,
    protected apprenantService: ApprenantService,
    protected etablissementService: EtablissementService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ demande }) => {
      this.updateForm(demande);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const demande = this.createFromForm();
    if (demande.id !== undefined) {
      this.subscribeToSaveResponse(this.demandeService.update(demande));
    } else {
      this.subscribeToSaveResponse(this.demandeService.create(demande));
    }
  }

  trackEnseignantById(index: number, item: IEnseignant): number {
    return item.id!;
  }

  trackApprenantById(index: number, item: IApprenant): number {
    return item.id!;
  }

  trackEtablissementById(index: number, item: IEtablissement): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDemande>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(demande: IDemande): void {
    this.editForm.patchValue({
      id: demande.id,
      motif: demande.motif,
      typeDemandeur: demande.typeDemandeur,
      nom: demande.nom,
      prenom: demande.prenom,
      nomEtab: demande.nomEtab,
      autreNomEtab: demande.autreNomEtab,
      email: demande.email,
      enseignant: demande.enseignant,
      apprenant: demande.apprenant,
      etablissement: demande.etablissement,
    });

    this.enseignantsSharedCollection = this.enseignantService.addEnseignantToCollectionIfMissing(
      this.enseignantsSharedCollection,
      demande.enseignant
    );
    this.apprenantsSharedCollection = this.apprenantService.addApprenantToCollectionIfMissing(
      this.apprenantsSharedCollection,
      demande.apprenant
    );
    this.etablissementsSharedCollection = this.etablissementService.addEtablissementToCollectionIfMissing(
      this.etablissementsSharedCollection,
      demande.etablissement
    );
  }

  protected loadRelationshipsOptions(): void {
    this.enseignantService
      .query()
      .pipe(map((res: HttpResponse<IEnseignant[]>) => res.body ?? []))
      .pipe(
        map((enseignants: IEnseignant[]) =>
          this.enseignantService.addEnseignantToCollectionIfMissing(enseignants, this.editForm.get('enseignant')!.value)
        )
      )
      .subscribe((enseignants: IEnseignant[]) => (this.enseignantsSharedCollection = enseignants));

    this.apprenantService
      .query()
      .pipe(map((res: HttpResponse<IApprenant[]>) => res.body ?? []))
      .pipe(
        map((apprenants: IApprenant[]) =>
          this.apprenantService.addApprenantToCollectionIfMissing(apprenants, this.editForm.get('apprenant')!.value)
        )
      )
      .subscribe((apprenants: IApprenant[]) => (this.apprenantsSharedCollection = apprenants));

    this.etablissementService
      .query()
      .pipe(map((res: HttpResponse<IEtablissement[]>) => res.body ?? []))
      .pipe(
        map((etablissements: IEtablissement[]) =>
          this.etablissementService.addEtablissementToCollectionIfMissing(etablissements, this.editForm.get('etablissement')!.value)
        )
      )
      .subscribe((etablissements: IEtablissement[]) => (this.etablissementsSharedCollection = etablissements));
  }

  protected createFromForm(): IDemande {
    return {
      ...new Demande(),
      id: this.editForm.get(['id'])!.value,
      motif: this.editForm.get(['motif'])!.value,
      typeDemandeur: this.editForm.get(['typeDemandeur'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      prenom: this.editForm.get(['prenom'])!.value,
      nomEtab: this.editForm.get(['nomEtab'])!.value,
      autreNomEtab: this.editForm.get(['autreNomEtab'])!.value,
      email: this.editForm.get(['email'])!.value,
      enseignant: this.editForm.get(['enseignant'])!.value,
      apprenant: this.editForm.get(['apprenant'])!.value,
      etablissement: this.editForm.get(['etablissement'])!.value,
    };
  }
}
