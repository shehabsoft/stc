import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPermissionUsersGroup, NewPermissionUsersGroup } from '../permission-users-group.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPermissionUsersGroup for edit and NewPermissionUsersGroupFormGroupInput for create.
 */
type PermissionUsersGroupFormGroupInput = IPermissionUsersGroup | PartialWithRequiredKeyOf<NewPermissionUsersGroup>;

type PermissionUsersGroupFormDefaults = Pick<NewPermissionUsersGroup, 'id'>;

type PermissionUsersGroupFormGroupContent = {
  id: FormControl<IPermissionUsersGroup['id'] | NewPermissionUsersGroup['id']>;
  userEmail: FormControl<IPermissionUsersGroup['userEmail']>;
  permissionLevel: FormControl<IPermissionUsersGroup['permissionLevel']>;
  permissionGroup: FormControl<IPermissionUsersGroup['permissionGroup']>;
};

export type PermissionUsersGroupFormGroup = FormGroup<PermissionUsersGroupFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PermissionUsersGroupFormService {
  createPermissionUsersGroupFormGroup(
    permissionUsersGroup: PermissionUsersGroupFormGroupInput = { id: null }
  ): PermissionUsersGroupFormGroup {
    const permissionUsersGroupRawValue = {
      ...this.getFormDefaults(),
      ...permissionUsersGroup,
    };
    return new FormGroup<PermissionUsersGroupFormGroupContent>({
      id: new FormControl(
        { value: permissionUsersGroupRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      userEmail: new FormControl(permissionUsersGroupRawValue.userEmail),
      permissionLevel: new FormControl(permissionUsersGroupRawValue.permissionLevel),
      permissionGroup: new FormControl(permissionUsersGroupRawValue.permissionGroup),
    });
  }

  getPermissionUsersGroup(form: PermissionUsersGroupFormGroup): IPermissionUsersGroup | NewPermissionUsersGroup {
    return form.getRawValue() as IPermissionUsersGroup | NewPermissionUsersGroup;
  }

  resetForm(form: PermissionUsersGroupFormGroup, permissionUsersGroup: PermissionUsersGroupFormGroupInput): void {
    const permissionUsersGroupRawValue = { ...this.getFormDefaults(), ...permissionUsersGroup };
    form.reset(
      {
        ...permissionUsersGroupRawValue,
        id: { value: permissionUsersGroupRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PermissionUsersGroupFormDefaults {
    return {
      id: null,
    };
  }
}
