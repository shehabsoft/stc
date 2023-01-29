import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IItem } from '../item.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../item.test-samples';

import { ItemService } from './item.service';

const requireRestSample: IItem = {
  ...sampleWithRequiredData,
};

describe('Item Service', () => {
  let service: ItemService;
  let httpMock: HttpTestingController;
  let expectedResult: IItem | IItem[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ItemService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Item', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const item = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(item).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Item', () => {
      const item = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(item).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Item', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Item', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Item', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addItemToCollectionIfMissing', () => {
      it('should add a Item to an empty array', () => {
        const item: IItem = sampleWithRequiredData;
        expectedResult = service.addItemToCollectionIfMissing([], item);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(item);
      });

      it('should not add a Item to an array that contains it', () => {
        const item: IItem = sampleWithRequiredData;
        const itemCollection: IItem[] = [
          {
            ...item,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addItemToCollectionIfMissing(itemCollection, item);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Item to an array that doesn't contain it", () => {
        const item: IItem = sampleWithRequiredData;
        const itemCollection: IItem[] = [sampleWithPartialData];
        expectedResult = service.addItemToCollectionIfMissing(itemCollection, item);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(item);
      });

      it('should add only unique Item to an array', () => {
        const itemArray: IItem[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const itemCollection: IItem[] = [sampleWithRequiredData];
        expectedResult = service.addItemToCollectionIfMissing(itemCollection, ...itemArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const item: IItem = sampleWithRequiredData;
        const item2: IItem = sampleWithPartialData;
        expectedResult = service.addItemToCollectionIfMissing([], item, item2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(item);
        expect(expectedResult).toContain(item2);
      });

      it('should accept null and undefined values', () => {
        const item: IItem = sampleWithRequiredData;
        expectedResult = service.addItemToCollectionIfMissing([], null, item, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(item);
      });

      it('should return initial array if no Item is added', () => {
        const itemCollection: IItem[] = [sampleWithRequiredData];
        expectedResult = service.addItemToCollectionIfMissing(itemCollection, undefined, null);
        expect(expectedResult).toEqual(itemCollection);
      });
    });

    describe('compareItem', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareItem(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareItem(entity1, entity2);
        const compareResult2 = service.compareItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareItem(entity1, entity2);
        const compareResult2 = service.compareItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareItem(entity1, entity2);
        const compareResult2 = service.compareItem(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
