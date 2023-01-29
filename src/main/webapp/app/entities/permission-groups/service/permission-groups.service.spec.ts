import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPermissionGroups } from '../permission-groups.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../permission-groups.test-samples';

import { PermissionGroupsService } from './permission-groups.service';

const requireRestSample: IPermissionGroups = {
  ...sampleWithRequiredData,
};

describe('PermissionGroups Service', () => {
  let service: PermissionGroupsService;
  let httpMock: HttpTestingController;
  let expectedResult: IPermissionGroups | IPermissionGroups[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PermissionGroupsService);
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

    it('should create a PermissionGroups', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const permissionGroups = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(permissionGroups).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PermissionGroups', () => {
      const permissionGroups = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(permissionGroups).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PermissionGroups', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PermissionGroups', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PermissionGroups', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPermissionGroupsToCollectionIfMissing', () => {
      it('should add a PermissionGroups to an empty array', () => {
        const permissionGroups: IPermissionGroups = sampleWithRequiredData;
        expectedResult = service.addPermissionGroupsToCollectionIfMissing([], permissionGroups);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(permissionGroups);
      });

      it('should not add a PermissionGroups to an array that contains it', () => {
        const permissionGroups: IPermissionGroups = sampleWithRequiredData;
        const permissionGroupsCollection: IPermissionGroups[] = [
          {
            ...permissionGroups,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPermissionGroupsToCollectionIfMissing(permissionGroupsCollection, permissionGroups);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PermissionGroups to an array that doesn't contain it", () => {
        const permissionGroups: IPermissionGroups = sampleWithRequiredData;
        const permissionGroupsCollection: IPermissionGroups[] = [sampleWithPartialData];
        expectedResult = service.addPermissionGroupsToCollectionIfMissing(permissionGroupsCollection, permissionGroups);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(permissionGroups);
      });

      it('should add only unique PermissionGroups to an array', () => {
        const permissionGroupsArray: IPermissionGroups[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const permissionGroupsCollection: IPermissionGroups[] = [sampleWithRequiredData];
        expectedResult = service.addPermissionGroupsToCollectionIfMissing(permissionGroupsCollection, ...permissionGroupsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const permissionGroups: IPermissionGroups = sampleWithRequiredData;
        const permissionGroups2: IPermissionGroups = sampleWithPartialData;
        expectedResult = service.addPermissionGroupsToCollectionIfMissing([], permissionGroups, permissionGroups2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(permissionGroups);
        expect(expectedResult).toContain(permissionGroups2);
      });

      it('should accept null and undefined values', () => {
        const permissionGroups: IPermissionGroups = sampleWithRequiredData;
        expectedResult = service.addPermissionGroupsToCollectionIfMissing([], null, permissionGroups, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(permissionGroups);
      });

      it('should return initial array if no PermissionGroups is added', () => {
        const permissionGroupsCollection: IPermissionGroups[] = [sampleWithRequiredData];
        expectedResult = service.addPermissionGroupsToCollectionIfMissing(permissionGroupsCollection, undefined, null);
        expect(expectedResult).toEqual(permissionGroupsCollection);
      });
    });

    describe('comparePermissionGroups', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePermissionGroups(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePermissionGroups(entity1, entity2);
        const compareResult2 = service.comparePermissionGroups(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePermissionGroups(entity1, entity2);
        const compareResult2 = service.comparePermissionGroups(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePermissionGroups(entity1, entity2);
        const compareResult2 = service.comparePermissionGroups(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
