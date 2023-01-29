import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFileData, NewFileData } from '../file-data.model';

export type PartialUpdateFileData = Partial<IFileData> & Pick<IFileData, 'id'>;

export type EntityResponseType = HttpResponse<IFileData>;
export type EntityArrayResponseType = HttpResponse<IFileData[]>;

@Injectable({ providedIn: 'root' })
export class FileDataService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/file-data');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(fileData: NewFileData): Observable<EntityResponseType> {
    return this.http.post<IFileData>(this.resourceUrl, fileData, { observe: 'response' });
  }

  update(fileData: IFileData): Observable<EntityResponseType> {
    return this.http.put<IFileData>(`${this.resourceUrl}/${this.getFileDataIdentifier(fileData)}`, fileData, { observe: 'response' });
  }

  partialUpdate(fileData: PartialUpdateFileData): Observable<EntityResponseType> {
    return this.http.patch<IFileData>(`${this.resourceUrl}/${this.getFileDataIdentifier(fileData)}`, fileData, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFileData>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFileData[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFileDataIdentifier(fileData: Pick<IFileData, 'id'>): number {
    return fileData.id;
  }

  compareFileData(o1: Pick<IFileData, 'id'> | null, o2: Pick<IFileData, 'id'> | null): boolean {
    return o1 && o2 ? this.getFileDataIdentifier(o1) === this.getFileDataIdentifier(o2) : o1 === o2;
  }

  addFileDataToCollectionIfMissing<Type extends Pick<IFileData, 'id'>>(
    fileDataCollection: Type[],
    ...fileDataToCheck: (Type | null | undefined)[]
  ): Type[] {
    const fileData: Type[] = fileDataToCheck.filter(isPresent);
    if (fileData.length > 0) {
      const fileDataCollectionIdentifiers = fileDataCollection.map(fileDataItem => this.getFileDataIdentifier(fileDataItem)!);
      const fileDataToAdd = fileData.filter(fileDataItem => {
        const fileDataIdentifier = this.getFileDataIdentifier(fileDataItem);
        if (fileDataCollectionIdentifiers.includes(fileDataIdentifier)) {
          return false;
        }
        fileDataCollectionIdentifiers.push(fileDataIdentifier);
        return true;
      });
      return [...fileDataToAdd, ...fileDataCollection];
    }
    return fileDataCollection;
  }
}
