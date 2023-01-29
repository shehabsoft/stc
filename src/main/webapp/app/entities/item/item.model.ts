import { IPermissionUsersGroup } from 'app/entities/permission-users-group/permission-users-group.model';
import { ItemType } from 'app/entities/enumerations/item-type.model';

export interface IItem {
  id: number;
  type?: ItemType | null;
  name?: string | null;
  permissionUsersGroup?: Pick<IPermissionUsersGroup, 'id'> | null;
}

export type NewItem = Omit<IItem, 'id'> & { id: null };
