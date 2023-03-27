import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BFPAService } from '../service/bfpa.service';
import { IBFPA, BFPA } from '../bfpa.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { BFPAUpdateComponent } from './bfpa-update.component';

describe('BFPA Management Update Component', () => {
  let comp: BFPAUpdateComponent;
  let fixture: ComponentFixture<BFPAUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let bFPAService: BFPAService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BFPAUpdateComponent],
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
      .overrideTemplate(BFPAUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BFPAUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bFPAService = TestBed.inject(BFPAService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const bFPA: IBFPA = { id: 456 };
      const user: IUser = { id: 43747 };
      bFPA.user = user;

      const userCollection: IUser[] = [{ id: 41095 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ bFPA });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const bFPA: IBFPA = { id: 456 };
      const user: IUser = { id: 68085 };
      bFPA.user = user;

      activatedRoute.data = of({ bFPA });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(bFPA));
      expect(comp.usersSharedCollection).toContain(user);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BFPA>>();
      const bFPA = { id: 123 };
      jest.spyOn(bFPAService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bFPA });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bFPA }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(bFPAService.update).toHaveBeenCalledWith(bFPA);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BFPA>>();
      const bFPA = new BFPA();
      jest.spyOn(bFPAService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bFPA });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bFPA }));
      saveSubject.complete();

      // THEN
      expect(bFPAService.create).toHaveBeenCalledWith(bFPA);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BFPA>>();
      const bFPA = { id: 123 };
      jest.spyOn(bFPAService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bFPA });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(bFPAService.update).toHaveBeenCalledWith(bFPA);
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
