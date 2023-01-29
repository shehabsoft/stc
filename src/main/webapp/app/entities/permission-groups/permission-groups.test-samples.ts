import { IPermissionGroups, NewPermissionGroups } from './permission-groups.model';

export const sampleWithRequiredData: IPermissionGroups = {
  id: 47856,
};

export const sampleWithPartialData: IPermissionGroups = {
  id: 85389,
};

export const sampleWithFullData: IPermissionGroups = {
  id: 13201,
  groupName: 'Representative multi-tasking transmitting',
};

export const sampleWithNewData: NewPermissionGroups = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
