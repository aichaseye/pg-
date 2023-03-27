import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ComptableMatiereService } from '../service/comptable-matiere.service';
import { IComptableMatiere, ComptableMatiere } from '../comptable-matiere.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { ComptableMatiereUpdateComponent } from './comptable-matiere-update.component';

describe('ComptableMatiere Management Update Component', () => {
  let comp: ComptableMatiereUpdateComponent;
  let fixture: ComponentFixture<ComptableMatiereUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let comptableMatiereService: ComptableMatiereService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ComptableMatiereUpdateComponent],
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
      .overrideTemplate(ComptableMatiereUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ComptableMatiereUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    comptableMatiereService = TestBed.inject(ComptableMatiereService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const comptableMatiere: IComptableMatiere = { id: 456 };
      const user: IUser = { id: 97608 };
      comptableMatiere.user = user;

      const userCollection: IUser[] = [{ id: 49442 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ comptableMatiere });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const comptableMatiere: IComptableMatiere = { id: 456 };
      const user: IUser = { id: 82137 };
      comptableMatiere.user = user;

      activatedRoute.data = of({ comptableMatiere });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(comptableMatiere));
      expect(comp.usersSharedCollection).toContain(user);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ComptableMatiere>>();
      const comptableMatiere = { id: 123 };
      jest.spyOn(comptableMatiereService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ comptableMatiere });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: comptableMatiere }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(comptableMatiereService.update).toHaveBeenCalledWith(comptableMatiere);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ComptableMatiere>>();
      const comptableMatiere = new ComptableMatiere();
      jest.spyOn(comptableMatiereService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ comptableMatiere });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: comptableMatiere }));
      saveSubject.complete();

      // THEN
      expect(comptableMatiereService.create).toHaveBeenCalledWith(comptableMatiere);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ComptableMatiere>>();
      const comptableMatiere = { id: 123 };
      jest.spyOn(comptableMatiereService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ comptableMatiere });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(comptableMatiereService.update).toHaveBeenCalledWith(comptableMatiere);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackUserById', () => {
      it('Should return tracked User primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUserById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
