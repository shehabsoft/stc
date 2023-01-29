import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../file-data.test-samples';

import { FileDataFormService } from './file-data-form.service';

describe('FileData Form Service', () => {
  let service: FileDataFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FileDataFormService);
  });

  describe('Service methods', () => {
    describe('createFileDataFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFileDataFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            binery: expect.any(Object),
            item: expect.any(Object),
          })
        );
      });

      it('passing IFileData should create a new form with FormGroup', () => {
        const formGroup = service.createFileDataFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            binery: expect.any(Object),
            item: expect.any(Object),
          })
        );
      });
    });

    describe('getFileData', () => {
      it('should return NewFileData for default FileData initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createFileDataFormGroup(sampleWithNewData);

        const fileData = service.getFileData(formGroup) as any;

        expect(fileData).toMatchObject(sampleWithNewData);
      });

      it('should return NewFileData for empty FileData initial value', () => {
        const formGroup = service.createFileDataFormGroup();

        const fileData = service.getFileData(formGroup) as any;

        expect(fileData).toMatchObject({});
      });

      it('should return IFileData', () => {
        const formGroup = service.createFileDataFormGroup(sampleWithRequiredData);

        const fileData = service.getFileData(formGroup) as any;

        expect(fileData).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFileData should not enable id FormControl', () => {
        const formGroup = service.createFileDataFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFileData should disable id FormControl', () => {
        const formGroup = service.createFileDataFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
