import { PermissionLevel } from 'app/entities/enumerations/permission-level.model';

import { IPermissionUsersGroup, NewPermissionUsersGroup } from './permission-users-group.model';

export const sampleWithRequiredData: IPermissionUsersGroup = {
  id: 94243,
};

export const sampleWithPartialData: IPermissionUsersGroup = {
  id: 82772,
  userEmail: 'Account',
  permissionLevel: PermissionLevel['EDIT'],
};

export const sampleWithFullData: IPermissionUsersGroup = {
  id: 34241,
  userEmail: 'Intelligent Developer',
  permissionLevel: PermissionLevel['EDIT'],
};

export const sampleWithNewData: NewPermissionUsersGroup = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
