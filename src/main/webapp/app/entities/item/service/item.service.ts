import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IItem, NewItem } from '../item.model';

export type PartialUpdateItem = Partial<IItem> & Pick<IItem, 'id'>;

export type EntityResponseType = HttpResponse<IItem>;
export type EntityArrayResponseType = HttpResponse<IItem[]>;

@Injectable({ providedIn: 'root' })
export class ItemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/items');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(item: NewItem): Observable<EntityResponseType> {
    return this.http.post<IItem>(this.resourceUrl, item, { observe: 'response' });
  }

  update(item: IItem): Observable<EntityResponseType> {
    return this.http.put<IItem>(`${this.resourceUrl}/${this.getItemIdentifier(item)}`, item, { observe: 'response' });
  }

  partialUpdate(item: PartialUpdateItem): Observable<EntityResponseType> {
    return this.http.patch<IItem>(`${this.resourceUrl}/${this.getItemIdentifier(item)}`, item, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IItem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getItemIdentifier(item: Pick<IItem, 'id'>): number {
    return item.id;
  }

  compareItem(o1: Pick<IItem, 'id'> | null, o2: Pick<IItem, 'id'> | null): boolean {
    return o1 && o2 ? this.getItemIdentifier(o1) === this.getItemIdentifier(o2) : o1 === o2;
  }

  addItemToCollectionIfMissing<Type extends Pick<IItem, 'id'>>(
    itemCollection: Type[],
    ...itemsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const items: Type[] = itemsToCheck.filter(isPresent);
    if (items.length > 0) {
      const itemCollectionIdentifiers = itemCollection.map(itemItem => this.getItemIdentifier(itemItem)!);
      const itemsToAdd = items.filter(itemItem => {
        const itemIdentifier = this.getItemIdentifier(itemItem);
        if (itemCollectionIdentifiers.includes(itemIdentifier)) {
          return false;
        }
        itemCollectionIdentifiers.push(itemIdentifier);
        return true;
      });
      return [...itemsToAdd, ...itemCollection];
    }
    return itemCollection;
  }
}
