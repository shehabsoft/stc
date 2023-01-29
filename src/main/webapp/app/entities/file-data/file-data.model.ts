import { IItem } from 'app/entities/item/item.model';

export interface IFileData {
  id: number;
  binery?: string | null;
  bineryContentType?: string | null;
  item?: Pick<IItem, 'id'> | null;
}

export type NewFileData = Omit<IFileData, 'id'> & { id: null };
