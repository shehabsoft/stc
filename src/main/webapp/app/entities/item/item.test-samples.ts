import { ItemType } from 'app/entities/enumerations/item-type.model';

import { IItem, NewItem } from './item.model';

export const sampleWithRequiredData: IItem = {
  id: 89800,
};

export const sampleWithPartialData: IItem = {
  id: 31792,
  type: ItemType['FILE'],
};

export const sampleWithFullData: IItem = {
  id: 76121,
  type: ItemType['FILE'],
  name: 'Meadows Account Investor',
};

export const sampleWithNewData: NewItem = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
