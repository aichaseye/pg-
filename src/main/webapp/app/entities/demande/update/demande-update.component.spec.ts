import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DemandeService } from '../service/demande.service';
import { IDemande, Demande } from '../demande.model';
import { IEnseignant } from 'app/entities/enseignant/enseignant.model';
import { EnseignantService } from 'app/entities/enseignant/service/enseignant.service';
import { IApprenant } from 'app/entities/apprenant/apprenant.model';
import { ApprenantService } from 'app/entities/apprenant/service/apprenant.service';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { EtablissementService } from 'app/entities/etablissement/service/etablissement.service';

import { DemandeUpdateComponent } from './demande-update.component';

describe('Demande Management Update Component', () => {
  let comp: DemandeUpdateComponent;
  let fixture: ComponentFixture<DemandeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let demandeService: DemandeService;
  let enseignantService: EnseignantService;
  let apprenantService: ApprenantService;
  let etablissementService: EtablissementService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DemandeUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(DemandeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DemandeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    demandeService = TestBed.inject(DemandeService);
    enseignantService = TestBed.inject(EnseignantService);
    apprenantService = TestBed.inject(ApprenantService);
    etablissementService = TestBed.inject(EtablissementService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Enseignant query and add missing value', () => {
      const demande: IDemande = { id: 456 };
      const enseignant: IEnseignant = { id: 45998 };
      demande.enseignant = enseignant;

      const enseignantCollection: IEnseignant[] = [{ id: 52773 }];
      jest.spyOn(enseignantService, 'query').mockReturnValue(of(new HttpResponse({ body: enseignantCollection })));
      const additionalEnseignants = [enseignant];
      const expectedCollection: IEnseignant[] = [...additionalEnseignants, ...enseignantCollection];
      jest.spyOn(enseignantService, 'addEnseignantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ demande });
      comp.ngOnInit();

      expect(enseignantService.query).toHaveBeenCalled();
      expect(enseignantService.addEnseignantToCollectionIfMissing).toHaveBeenCalledWith(enseignantCollection, ...additionalEnseignants);
      expect(comp.enseignantsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Apprenant query and add missing value', () => {
      const demande: IDemande = { id: 456 };
      const apprenant: IApprenant = { id: 9690 };
      demande.apprenant = apprenant;

      const apprenantCollection: IApprenant[] = [{ id: 76235 }];
      jest.spyOn(apprenantService, 'query').mockReturnValue(of(new HttpResponse({ body: apprenantCollection })));
      const additionalApprenants = [apprenant];
      const expectedCollection: IApprenant[] = [...additionalApprenants, ...apprenantCollection];
      jest.spyOn(apprenantService, 'addApprenantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ demande });
      comp.ngOnInit();

      expect(apprenantService.query).toHaveBeenCalled();
      expect(apprenantService.addApprenantToCollectionIfMissing).toHaveBeenCalledWith(apprenantCollection, ...additionalApprenants);
      expect(comp.apprenantsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Etablissement query and add missing value', () => {
      const demande: IDemande = { id: 456 };
      const etablissement: IEtablissement = { id: 98001 };
      demande.etablissement = etablissement;

      const etablissementCollection: IEtablissement[] = [{ id: 2744 }];
      jest.spyOn(etablissementService, 'query').mockReturnValue(of(new HttpResponse({ body: etablissementCollection })));
      const additionalEtablissements = [etablissement];
      const expectedCollection: IEtablissement[] = [...additionalEtablissements, ...etablissementCollection];
      jest.spyOn(etablissementService, 'addEtablissementToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ demande });
      comp.ngOnInit();

      expect(etablissementService.query).toHaveBeenCalled();
      expect(etablissementService.addEtablissementToCollectionIfMissing).toHaveBeenCalledWith(
        etablissementCollection,
        ...additionalEtablissements
      );
      expect(comp.etablissementsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const demande: IDemande = { id: 456 };
      const enseignant: IEnseignant = { id: 81460 };
      demande.enseignant = enseignant;
      const apprenant: IApprenant = { id: 85050 };
      demande.apprenant = apprenant;
      const etablissement: IEtablissement = { id: 51377 };
      demande.etablissement = etablissement;

      activatedRoute.data = of({ demande });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(demande));
      expect(comp.enseignantsSharedCollection).toContain(enseignant);
      expect(comp.apprenantsSharedCollection).toContain(apprenant);
      expect(comp.etablissementsSharedCollection).toContain(etablissement);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Demande>>();
      const demande = { id: 123 };
      jest.spyOn(demandeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ demande });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: demande }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(demandeService.update).toHaveBeenCalledWith(demande);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Demande>>();
      const demande = new Demande();
      jest.spyOn(demandeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ demande });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: demande }));
      saveSubject.complete();

      // THEN
      expect(demandeService.create).toHaveBeenCalledWith(demande);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Demande>>();
      const demande = { id: 123 };
      jest.spyOn(demandeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ demande });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(demandeService.update).toHaveBeenCalledWith(demande);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackEnseignantById', () => {
      it('Should return tracked Enseignant primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEnseignantById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackApprenantById', () => {
      it('Should return tracked Apprenant primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackApprenantById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackEtablissementById', () => {
      it('Should return tracked Etablissement primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEtablissementById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
