import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../permission-groups.test-samples';

import { PermissionGroupsFormService } from './permission-groups-form.service';

describe('PermissionGroups Form Service', () => {
  let service: PermissionGroupsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PermissionGroupsFormService);
  });

  describe('Service methods', () => {
    describe('createPermissionGroupsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPermissionGroupsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            groupName: expect.any(Object),
          })
        );
      });

      it('passing IPermissionGroups should create a new form with FormGroup', () => {
        const formGroup = service.createPermissionGroupsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            groupName: expect.any(Object),
          })
        );
      });
    });

    describe('getPermissionGroups', () => {
      it('should return NewPermissionGroups for default PermissionGroups initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPermissionGroupsFormGroup(sampleWithNewData);

        const permissionGroups = service.getPermissionGroups(formGroup) as any;

        expect(permissionGroups).toMatchObject(sampleWithNewData);
      });

      it('should return NewPermissionGroups for empty PermissionGroups initial value', () => {
        const formGroup = service.createPermissionGroupsFormGroup();

        const permissionGroups = service.getPermissionGroups(formGroup) as any;

        expect(permissionGroups).toMatchObject({});
      });

      it('should return IPermissionGroups', () => {
        const formGroup = service.createPermissionGroupsFormGroup(sampleWithRequiredData);

        const permissionGroups = service.getPermissionGroups(formGroup) as any;

        expect(permissionGroups).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPermissionGroups should not enable id FormControl', () => {
        const formGroup = service.createPermissionGroupsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPermissionGroups should disable id FormControl', () => {
        const formGroup = service.createPermissionGroupsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
