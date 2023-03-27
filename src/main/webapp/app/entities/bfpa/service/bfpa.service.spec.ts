import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBFPA, BFPA } from '../bfpa.model';

import { BFPAService } from './bfpa.service';

describe('BFPA Service', () => {
  let service: BFPAService;
  let httpMock: HttpTestingController;
  let elemDefault: IBFPA;
  let expectedResult: IBFPA | IBFPA[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BFPAService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nomPrenom: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a BFPA', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new BFPA()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BFPA', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomPrenom: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BFPA', () => {
      const patchObject = Object.assign(
        {
          nomPrenom: 'BBBBBB',
        },
        new BFPA()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BFPA', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomPrenom: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a BFPA', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addBFPAToCollectionIfMissing', () => {
      it('should add a BFPA to an empty array', () => {
        const bFPA: IBFPA = { id: 123 };
        expectedResult = service.addBFPAToCollectionIfMissing([], bFPA);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bFPA);
      });

      it('should not add a BFPA to an array that contains it', () => {
        const bFPA: IBFPA = { id: 123 };
        const bFPACollection: IBFPA[] = [
          {
            ...bFPA,
          },
          { id: 456 },
        ];
        expectedResult = service.addBFPAToCollectionIfMissing(bFPACollection, bFPA);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BFPA to an array that doesn't contain it", () => {
        const bFPA: IBFPA = { id: 123 };
        const bFPACollection: IBFPA[] = [{ id: 456 }];
        expectedResult = service.addBFPAToCollectionIfMissing(bFPACollection, bFPA);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bFPA);
      });

      it('should add only unique BFPA to an array', () => {
        const bFPAArray: IBFPA[] = [{ id: 123 }, { id: 456 }, { id: 72416 }];
        const bFPACollection: IBFPA[] = [{ id: 123 }];
        expectedResult = service.addBFPAToCollectionIfMissing(bFPACollection, ...bFPAArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const bFPA: IBFPA = { id: 123 };
        const bFPA2: IBFPA = { id: 456 };
        expectedResult = service.addBFPAToCollectionIfMissing([], bFPA, bFPA2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bFPA);
        expect(expectedResult).toContain(bFPA2);
      });

      it('should accept null and undefined values', () => {
        const bFPA: IBFPA = { id: 123 };
        expectedResult = service.addBFPAToCollectionIfMissing([], null, bFPA, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bFPA);
      });

      it('should return initial array if no BFPA is added', () => {
        const bFPACollection: IBFPA[] = [{ id: 123 }];
        expectedResult = service.addBFPAToCollectionIfMissing(bFPACollection, undefined, null);
        expect(expectedResult).toEqual(bFPACollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
