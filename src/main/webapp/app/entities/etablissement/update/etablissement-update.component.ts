import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IEtablissement, Etablissement } from '../etablissement.model';
import { EtablissementService } from '../service/etablissement.service';
import { IBFPA } from 'app/entities/bfpa/bfpa.model';
import { BFPAService } from 'app/entities/bfpa/service/bfpa.service';
import { NomReg } from 'app/entities/enumerations/nom-reg.model';
import { NomDep } from 'app/entities/enumerations/nom-dep.model';
import { NomEtab } from 'app/entities/enumerations/nom-etab.model';
import { CodeIA } from 'app/entities/enumerations/code-ia.model';
import { TypeEtab } from 'app/entities/enumerations/type-etab.model';
import { StatutEtab } from 'app/entities/enumerations/statut-etab.model';
import { TypeInspection } from 'app/entities/enumerations/type-inspection.model';

@Component({
  selector: 'jhi-etablissement-update',
  templateUrl: './etablissement-update.component.html',
})
export class EtablissementUpdateComponent implements OnInit {
  isSaving = false;
  nomRegValues = Object.keys(NomReg);
  nomDepValues = Object.keys(NomDep);
  nomEtabValues = Object.keys(NomEtab);
  codeIAValues = Object.keys(CodeIA);
  typeEtabValues = Object.keys(TypeEtab);
  statutEtabValues = Object.keys(StatutEtab);
  typeInspectionValues = Object.keys(TypeInspection);

  bFPASSharedCollection: IBFPA[] = [];

  editForm = this.fb.group({
    id: [],
    matriculeEtab: [null, []],
    region: [null, [Validators.required]],
    departement: [null, [Validators.required]],
    autreRegion: [],
    autreDep: [],
    nomEtab: [null, [Validators.required]],
    codeIA: [null, [Validators.required]],
    autreCodeIA: [],
    autreNomeEtab: [null, []],
    typeEtab: [null, [Validators.required]],
    anneeCreation: [null, [Validators.required]],
    statut: [null, [Validators.required]],
    emailEtab: [null, [Validators.required]],
    typeInsp: [],
    bFPA: [],
  });

  constructor(
    protected etablissementService: EtablissementService,
    protected bFPAService: BFPAService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ etablissement }) => {
      this.updateForm(etablissement);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const etablissement = this.createFromForm();
    if (etablissement.id !== undefined) {
      this.subscribeToSaveResponse(this.etablissementService.update(etablissement));
    } else {
      this.subscribeToSaveResponse(this.etablissementService.create(etablissement));
    }
  }

  trackBFPAById(index: number, item: IBFPA): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEtablissement>>): void {
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

  protected updateForm(etablissement: IEtablissement): void {
    this.editForm.patchValue({
      id: etablissement.id,
      matriculeEtab: etablissement.matriculeEtab,
      region: etablissement.region,
      departement: etablissement.departement,
      autreRegion: etablissement.autreRegion,
      autreDep: etablissement.autreDep,
      nomEtab: etablissement.nomEtab,
      codeIA: etablissement.codeIA,
      autreCodeIA: etablissement.autreCodeIA,
      autreNomeEtab: etablissement.autreNomeEtab,
      typeEtab: etablissement.typeEtab,
      anneeCreation: etablissement.anneeCreation,
      statut: etablissement.statut,
      emailEtab: etablissement.emailEtab,
      typeInsp: etablissement.typeInsp,
      bFPA: etablissement.bFPA,
    });

    this.bFPASSharedCollection = this.bFPAService.addBFPAToCollectionIfMissing(this.bFPASSharedCollection, etablissement.bFPA);
  }

  protected loadRelationshipsOptions(): void {
    this.bFPAService
      .query()
      .pipe(map((res: HttpResponse<IBFPA[]>) => res.body ?? []))
      .pipe(map((bFPAS: IBFPA[]) => this.bFPAService.addBFPAToCollectionIfMissing(bFPAS, this.editForm.get('bFPA')!.value)))
      .subscribe((bFPAS: IBFPA[]) => (this.bFPASSharedCollection = bFPAS));
  }

  protected createFromForm(): IEtablissement {
    return {
      ...new Etablissement(),
      id: this.editForm.get(['id'])!.value,
      matriculeEtab: this.editForm.get(['matriculeEtab'])!.value,
      region: this.editForm.get(['region'])!.value,
      departement: this.editForm.get(['departement'])!.value,
      autreRegion: this.editForm.get(['autreRegion'])!.value,
      autreDep: this.editForm.get(['autreDep'])!.value,
      nomEtab: this.editForm.get(['nomEtab'])!.value,
      codeIA: this.editForm.get(['codeIA'])!.value,
      autreCodeIA: this.editForm.get(['autreCodeIA'])!.value,
      autreNomeEtab: this.editForm.get(['autreNomeEtab'])!.value,
      typeEtab: this.editForm.get(['typeEtab'])!.value,
      anneeCreation: this.editForm.get(['anneeCreation'])!.value,
      statut: this.editForm.get(['statut'])!.value,
      emailEtab: this.editForm.get(['emailEtab'])!.value,
      typeInsp: this.editForm.get(['typeInsp'])!.value,
      bFPA: this.editForm.get(['bFPA'])!.value,
    };
  }
}
