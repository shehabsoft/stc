import { IFileData, NewFileData } from './file-data.model';

export const sampleWithRequiredData: IFileData = {
  id: 66468,
};

export const sampleWithPartialData: IFileData = {
  id: 79769,
  binery: '../fake-data/blob/hipster.png',
  bineryContentType: 'unknown',
};

export const sampleWithFullData: IFileData = {
  id: 8546,
  binery: '../fake-data/blob/hipster.png',
  bineryContentType: 'unknown',
};

export const sampleWithNewData: NewFileData = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
