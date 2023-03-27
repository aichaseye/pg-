import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ApprenantService } from '../service/apprenant.service';
import { IApprenant, Apprenant } from '../apprenant.model';
import { IChefEtablissement } from 'app/entities/chef-etablissement/chef-etablissement.model';
import { ChefEtablissementService } from 'app/entities/chef-etablissement/service/chef-etablissement.service';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { EtablissementService } from 'app/entities/etablissement/service/etablissement.service';

import { ApprenantUpdateComponent } from './apprenant-update.component';

describe('Apprenant Management Update Component', () => {
  let comp: ApprenantUpdateComponent;
  let fixture: ComponentFixture<ApprenantUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let apprenantService: ApprenantService;
  let chefEtablissementService: ChefEtablissementService;
  let etablissementService: EtablissementService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ApprenantUpdateComponent],
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
      .overrideTemplate(ApprenantUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ApprenantUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    apprenantService = TestBed.inject(ApprenantService);
    chefEtablissementService = TestBed.inject(ChefEtablissementService);
    etablissementService = TestBed.inject(EtablissementService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ChefEtablissement query and add missing value', () => {
      const apprenant: IApprenant = { id: 456 };
      const chefEtablissement: IChefEtablissement = { id: 50415 };
      apprenant.chefEtablissement = chefEtablissement;

      const chefEtablissementCollection: IChefEtablissement[] = [{ id: 22173 }];
      jest.spyOn(chefEtablissementService, 'query').mockReturnValue(of(new HttpResponse({ body: chefEtablissementCollection })));
      const additionalChefEtablissements = [chefEtablissement];
      const expectedCollection: IChefEtablissement[] = [...additionalChefEtablissements, ...chefEtablissementCollection];
      jest.spyOn(chefEtablissementService, 'addChefEtablissementToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ apprenant });
      comp.ngOnInit();

      expect(chefEtablissementService.query).toHaveBeenCalled();
      expect(chefEtablissementService.addChefEtablissementToCollectionIfMissing).toHaveBeenCalledWith(
        chefEtablissementCollection,
        ...additionalChefEtablissements
      );
      expect(comp.chefEtablissementsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Etablissement query and add missing value', () => {
      const apprenant: IApprenant = { id: 456 };
      const etablissement: IEtablissement = { id: 92984 };
      apprenant.etablissement = etablissement;

      const etablissementCollection: IEtablissement[] = [{ id: 4021 }];
      jest.spyOn(etablissementService, 'query').mockReturnValue(of(new HttpResponse({ body: etablissementCollection })));
      const additionalEtablissements = [etablissement];
      const expectedCollection: IEtablissement[] = [...additionalEtablissements, ...etablissementCollection];
      jest.spyOn(etablissementService, 'addEtablissementToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ apprenant });
      comp.ngOnInit();

      expect(etablissementService.query).toHaveBeenCalled();
      expect(etablissementService.addEtablissementToCollectionIfMissing).toHaveBeenCalledWith(
        etablissementCollection,
        ...additionalEtablissements
      );
      expect(comp.etablissementsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const apprenant: IApprenant = { id: 456 };
      const chefEtablissement: IChefEtablissement = { id: 65419 };
      apprenant.chefEtablissement = chefEtablissement;
      const etablissement: IEtablissement = { id: 36316 };
      apprenant.etablissement = etablissement;

      activatedRoute.data = of({ apprenant });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(apprenant));
      expect(comp.chefEtablissementsSharedCollection).toContain(chefEtablissement);
      expect(comp.etablissementsSharedCollection).toContain(etablissement);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Apprenant>>();
      const apprenant = { id: 123 };
      jest.spyOn(apprenantService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ apprenant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: apprenant }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(apprenantService.update).toHaveBeenCalledWith(apprenant);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Apprenant>>();
      const apprenant = new Apprenant();
      jest.spyOn(apprenantService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ apprenant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: apprenant }));
      saveSubject.complete();

      // THEN
      expect(apprenantService.create).toHaveBeenCalledWith(apprenant);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Apprenant>>();
      const apprenant = { id: 123 };
      jest.spyOn(apprenantService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ apprenant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(apprenantService.update).toHaveBeenCalledWith(apprenant);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackChefEtablissementById', () => {
      it('Should return tracked ChefEtablissement primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackChefEtablissementById(0, entity);
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
