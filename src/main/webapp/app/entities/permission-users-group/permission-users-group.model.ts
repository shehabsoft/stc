import { IPermissionGroups } from 'app/entities/permission-groups/permission-groups.model';
import { PermissionLevel } from 'app/entities/enumerations/permission-level.model';

export interface IPermissionUsersGroup {
  id: number;
  userEmail?: string | null;
  permissionLevel?: PermissionLevel | null;
  permissionGroup?: Pick<IPermissionGroups, 'id'> | null;
}

export type NewPermissionUsersGroup = Omit<IPermissionUsersGroup, 'id'> & { id: null };
