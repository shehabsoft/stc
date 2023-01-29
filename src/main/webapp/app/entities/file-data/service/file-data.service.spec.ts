import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFileData } from '../file-data.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../file-data.test-samples';

import { FileDataService } from './file-data.service';

const requireRestSample: IFileData = {
  ...sampleWithRequiredData,
};

describe('FileData Service', () => {
  let service: FileDataService;
  let httpMock: HttpTestingController;
  let expectedResult: IFileData | IFileData[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FileDataService);
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

    it('should create a FileData', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const fileData = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(fileData).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FileData', () => {
      const fileData = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(fileData).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FileData', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FileData', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FileData', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFileDataToCollectionIfMissing', () => {
      it('should add a FileData to an empty array', () => {
        const fileData: IFileData = sampleWithRequiredData;
        expectedResult = service.addFileDataToCollectionIfMissing([], fileData);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fileData);
      });

      it('should not add a FileData to an array that contains it', () => {
        const fileData: IFileData = sampleWithRequiredData;
        const fileDataCollection: IFileData[] = [
          {
            ...fileData,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFileDataToCollectionIfMissing(fileDataCollection, fileData);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FileData to an array that doesn't contain it", () => {
        const fileData: IFileData = sampleWithRequiredData;
        const fileDataCollection: IFileData[] = [sampleWithPartialData];
        expectedResult = service.addFileDataToCollectionIfMissing(fileDataCollection, fileData);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fileData);
      });

      it('should add only unique FileData to an array', () => {
        const fileDataArray: IFileData[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const fileDataCollection: IFileData[] = [sampleWithRequiredData];
        expectedResult = service.addFileDataToCollectionIfMissing(fileDataCollection, ...fileDataArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const fileData: IFileData = sampleWithRequiredData;
        const fileData2: IFileData = sampleWithPartialData;
        expectedResult = service.addFileDataToCollectionIfMissing([], fileData, fileData2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fileData);
        expect(expectedResult).toContain(fileData2);
      });

      it('should accept null and undefined values', () => {
        const fileData: IFileData = sampleWithRequiredData;
        expectedResult = service.addFileDataToCollectionIfMissing([], null, fileData, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fileData);
      });

      it('should return initial array if no FileData is added', () => {
        const fileDataCollection: IFileData[] = [sampleWithRequiredData];
        expectedResult = service.addFileDataToCollectionIfMissing(fileDataCollection, undefined, null);
        expect(expectedResult).toEqual(fileDataCollection);
      });
    });

    describe('compareFileData', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFileData(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFileData(entity1, entity2);
        const compareResult2 = service.compareFileData(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFileData(entity1, entity2);
        const compareResult2 = service.compareFileData(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFileData(entity1, entity2);
        const compareResult2 = service.compareFileData(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
