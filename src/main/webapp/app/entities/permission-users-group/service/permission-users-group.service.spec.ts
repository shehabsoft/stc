import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPermissionUsersGroup } from '../permission-users-group.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../permission-users-group.test-samples';

import { PermissionUsersGroupService } from './permission-users-group.service';

const requireRestSample: IPermissionUsersGroup = {
  ...sampleWithRequiredData,
};

describe('PermissionUsersGroup Service', () => {
  let service: PermissionUsersGroupService;
  let httpMock: HttpTestingController;
  let expectedResult: IPermissionUsersGroup | IPermissionUsersGroup[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PermissionUsersGroupService);
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

    it('should create a PermissionUsersGroup', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const permissionUsersGroup = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(permissionUsersGroup).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PermissionUsersGroup', () => {
      const permissionUsersGroup = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(permissionUsersGroup).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PermissionUsersGroup', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PermissionUsersGroup', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PermissionUsersGroup', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPermissionUsersGroupToCollectionIfMissing', () => {
      it('should add a PermissionUsersGroup to an empty array', () => {
        const permissionUsersGroup: IPermissionUsersGroup = sampleWithRequiredData;
        expectedResult = service.addPermissionUsersGroupToCollectionIfMissing([], permissionUsersGroup);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(permissionUsersGroup);
      });

      it('should not add a PermissionUsersGroup to an array that contains it', () => {
        const permissionUsersGroup: IPermissionUsersGroup = sampleWithRequiredData;
        const permissionUsersGroupCollection: IPermissionUsersGroup[] = [
          {
            ...permissionUsersGroup,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPermissionUsersGroupToCollectionIfMissing(permissionUsersGroupCollection, permissionUsersGroup);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PermissionUsersGroup to an array that doesn't contain it", () => {
        const permissionUsersGroup: IPermissionUsersGroup = sampleWithRequiredData;
        const permissionUsersGroupCollection: IPermissionUsersGroup[] = [sampleWithPartialData];
        expectedResult = service.addPermissionUsersGroupToCollectionIfMissing(permissionUsersGroupCollection, permissionUsersGroup);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(permissionUsersGroup);
      });

      it('should add only unique PermissionUsersGroup to an array', () => {
        const permissionUsersGroupArray: IPermissionUsersGroup[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const permissionUsersGroupCollection: IPermissionUsersGroup[] = [sampleWithRequiredData];
        expectedResult = service.addPermissionUsersGroupToCollectionIfMissing(permissionUsersGroupCollection, ...permissionUsersGroupArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const permissionUsersGroup: IPermissionUsersGroup = sampleWithRequiredData;
        const permissionUsersGroup2: IPermissionUsersGroup = sampleWithPartialData;
        expectedResult = service.addPermissionUsersGroupToCollectionIfMissing([], permissionUsersGroup, permissionUsersGroup2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(permissionUsersGroup);
        expect(expectedResult).toContain(permissionUsersGroup2);
      });

      it('should accept null and undefined values', () => {
        const permissionUsersGroup: IPermissionUsersGroup = sampleWithRequiredData;
        expectedResult = service.addPermissionUsersGroupToCollectionIfMissing([], null, permissionUsersGroup, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(permissionUsersGroup);
      });

      it('should return initial array if no PermissionUsersGroup is added', () => {
        const permissionUsersGroupCollection: IPermissionUsersGroup[] = [sampleWithRequiredData];
        expectedResult = service.addPermissionUsersGroupToCollectionIfMissing(permissionUsersGroupCollection, undefined, null);
        expect(expectedResult).toEqual(permissionUsersGroupCollection);
      });
    });

    describe('comparePermissionUsersGroup', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePermissionUsersGroup(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePermissionUsersGroup(entity1, entity2);
        const compareResult2 = service.comparePermissionUsersGroup(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePermissionUsersGroup(entity1, entity2);
        const compareResult2 = service.comparePermissionUsersGroup(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePermissionUsersGroup(entity1, entity2);
        const compareResult2 = service.comparePermissionUsersGroup(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
