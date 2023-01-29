export interface IPermissionGroups {
  id: number;
  groupName?: string | null;
}

export type NewPermissionGroups = Omit<IPermissionGroups, 'id'> & { id: null };
