import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../permission-users-group.test-samples';

import { PermissionUsersGroupFormService } from './permission-users-group-form.service';

describe('PermissionUsersGroup Form Service', () => {
  let service: PermissionUsersGroupFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PermissionUsersGroupFormService);
  });

  describe('Service methods', () => {
    describe('createPermissionUsersGroupFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPermissionUsersGroupFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            userEmail: expect.any(Object),
            permissionLevel: expect.any(Object),
            permissionGroup: expect.any(Object),
          })
        );
      });

      it('passing IPermissionUsersGroup should create a new form with FormGroup', () => {
        const formGroup = service.createPermissionUsersGroupFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            userEmail: expect.any(Object),
            permissionLevel: expect.any(Object),
            permissionGroup: expect.any(Object),
          })
        );
      });
    });

    describe('getPermissionUsersGroup', () => {
      it('should return NewPermissionUsersGroup for default PermissionUsersGroup initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPermissionUsersGroupFormGroup(sampleWithNewData);

        const permissionUsersGroup = service.getPermissionUsersGroup(formGroup) as any;

        expect(permissionUsersGroup).toMatchObject(sampleWithNewData);
      });

      it('should return NewPermissionUsersGroup for empty PermissionUsersGroup initial value', () => {
        const formGroup = service.createPermissionUsersGroupFormGroup();

        const permissionUsersGroup = service.getPermissionUsersGroup(formGroup) as any;

        expect(permissionUsersGroup).toMatchObject({});
      });

      it('should return IPermissionUsersGroup', () => {
        const formGroup = service.createPermissionUsersGroupFormGroup(sampleWithRequiredData);

        const permissionUsersGroup = service.getPermissionUsersGroup(formGroup) as any;

        expect(permissionUsersGroup).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPermissionUsersGroup should not enable id FormControl', () => {
        const formGroup = service.createPermissionUsersGroupFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPermissionUsersGroup should disable id FormControl', () => {
        const formGroup = service.createPermissionUsersGroupFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
