import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IReferant, Referant } from '../referant.model';

import { ReferantService } from './referant.service';

describe('Referant Service', () => {
  let service: ReferantService;
  let httpMock: HttpTestingController;
  let elemDefault: IReferant;
  let expectedResult: IReferant | IReferant[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ReferantService);
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

    it('should create a Referant', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Referant()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Referant', () => {
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

    it('should partial update a Referant', () => {
      const patchObject = Object.assign(
        {
          nomPrenom: 'BBBBBB',
        },
        new Referant()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Referant', () => {
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

    it('should delete a Referant', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addReferantToCollectionIfMissing', () => {
      it('should add a Referant to an empty array', () => {
        const referant: IReferant = { id: 123 };
        expectedResult = service.addReferantToCollectionIfMissing([], referant);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(referant);
      });

      it('should not add a Referant to an array that contains it', () => {
        const referant: IReferant = { id: 123 };
        const referantCollection: IReferant[] = [
          {
            ...referant,
          },
          { id: 456 },
        ];
        expectedResult = service.addReferantToCollectionIfMissing(referantCollection, referant);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Referant to an array that doesn't contain it", () => {
        const referant: IReferant = { id: 123 };
        const referantCollection: IReferant[] = [{ id: 456 }];
        expectedResult = service.addReferantToCollectionIfMissing(referantCollection, referant);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(referant);
      });

      it('should add only unique Referant to an array', () => {
        const referantArray: IReferant[] = [{ id: 123 }, { id: 456 }, { id: 8379 }];
        const referantCollection: IReferant[] = [{ id: 123 }];
        expectedResult = service.addReferantToCollectionIfMissing(referantCollection, ...referantArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const referant: IReferant = { id: 123 };
        const referant2: IReferant = { id: 456 };
        expectedResult = service.addReferantToCollectionIfMissing([], referant, referant2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(referant);
        expect(expectedResult).toContain(referant2);
      });

      it('should accept null and undefined values', () => {
        const referant: IReferant = { id: 123 };
        expectedResult = service.addReferantToCollectionIfMissing([], null, referant, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(referant);
      });

      it('should return initial array if no Referant is added', () => {
        const referantCollection: IReferant[] = [{ id: 123 }];
        expectedResult = service.addReferantToCollectionIfMissing(referantCollection, undefined, null);
        expect(expectedResult).toEqual(referantCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
