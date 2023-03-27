import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IComptableMatiere, ComptableMatiere } from '../comptable-matiere.model';
import { ComptableMatiereService } from '../service/comptable-matiere.service';

import { ComptableMatiereRoutingResolveService } from './comptable-matiere-routing-resolve.service';

describe('ComptableMatiere routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ComptableMatiereRoutingResolveService;
  let service: ComptableMatiereService;
  let resultComptableMatiere: IComptableMatiere | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(ComptableMatiereRoutingResolveService);
    service = TestBed.inject(ComptableMatiereService);
    resultComptableMatiere = undefined;
  });

  describe('resolve', () => {
    it('should return IComptableMatiere returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultComptableMatiere = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultComptableMatiere).toEqual({ id: 123 });
    });

    it('should return new IComptableMatiere if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultComptableMatiere = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultComptableMatiere).toEqual(new ComptableMatiere());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ComptableMatiere })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultComptableMatiere = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultComptableMatiere).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
