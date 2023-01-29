import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPermissionGroups, NewPermissionGroups } from '../permission-groups.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPermissionGroups for edit and NewPermissionGroupsFormGroupInput for create.
 */
type PermissionGroupsFormGroupInput = IPermissionGroups | PartialWithRequiredKeyOf<NewPermissionGroups>;

type PermissionGroupsFormDefaults = Pick<NewPermissionGroups, 'id'>;

type PermissionGroupsFormGroupContent = {
  id: FormControl<IPermissionGroups['id'] | NewPermissionGroups['id']>;
  groupName: FormControl<IPermissionGroups['groupName']>;
};

export type PermissionGroupsFormGroup = FormGroup<PermissionGroupsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PermissionGroupsFormService {
  createPermissionGroupsFormGroup(permissionGroups: PermissionGroupsFormGroupInput = { id: null }): PermissionGroupsFormGroup {
    const permissionGroupsRawValue = {
      ...this.getFormDefaults(),
      ...permissionGroups,
    };
    return new FormGroup<PermissionGroupsFormGroupContent>({
      id: new FormControl(
        { value: permissionGroupsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      groupName: new FormControl(permissionGroupsRawValue.groupName),
    });
  }

  getPermissionGroups(form: PermissionGroupsFormGroup): IPermissionGroups | NewPermissionGroups {
    return form.getRawValue() as IPermissionGroups | NewPermissionGroups;
  }

  resetForm(form: PermissionGroupsFormGroup, permissionGroups: PermissionGroupsFormGroupInput): void {
    const permissionGroupsRawValue = { ...this.getFormDefaults(), ...permissionGroups };
    form.reset(
      {
        ...permissionGroupsRawValue,
        id: { value: permissionGroupsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PermissionGroupsFormDefaults {
    return {
      id: null,
    };
  }
}
