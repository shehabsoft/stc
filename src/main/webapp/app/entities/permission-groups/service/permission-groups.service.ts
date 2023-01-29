import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPermissionGroups, NewPermissionGroups } from '../permission-groups.model';

export type PartialUpdatePermissionGroups = Partial<IPermissionGroups> & Pick<IPermissionGroups, 'id'>;

export type EntityResponseType = HttpResponse<IPermissionGroups>;
export type EntityArrayResponseType = HttpResponse<IPermissionGroups[]>;

@Injectable({ providedIn: 'root' })
export class PermissionGroupsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/permission-groups');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(permissionGroups: NewPermissionGroups): Observable<EntityResponseType> {
    return this.http.post<IPermissionGroups>(this.resourceUrl, permissionGroups, { observe: 'response' });
  }

  update(permissionGroups: IPermissionGroups): Observable<EntityResponseType> {
    return this.http.put<IPermissionGroups>(
      `${this.resourceUrl}/${this.getPermissionGroupsIdentifier(permissionGroups)}`,
      permissionGroups,
      { observe: 'response' }
    );
  }

  partialUpdate(permissionGroups: PartialUpdatePermissionGroups): Observable<EntityResponseType> {
    return this.http.patch<IPermissionGroups>(
      `${this.resourceUrl}/${this.getPermissionGroupsIdentifier(permissionGroups)}`,
      permissionGroups,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPermissionGroups>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPermissionGroups[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPermissionGroupsIdentifier(permissionGroups: Pick<IPermissionGroups, 'id'>): number {
    return permissionGroups.id;
  }

  comparePermissionGroups(o1: Pick<IPermissionGroups, 'id'> | null, o2: Pick<IPermissionGroups, 'id'> | null): boolean {
    return o1 && o2 ? this.getPermissionGroupsIdentifier(o1) === this.getPermissionGroupsIdentifier(o2) : o1 === o2;
  }

  addPermissionGroupsToCollectionIfMissing<Type extends Pick<IPermissionGroups, 'id'>>(
    permissionGroupsCollection: Type[],
    ...permissionGroupsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const permissionGroups: Type[] = permissionGroupsToCheck.filter(isPresent);
    if (permissionGroups.length > 0) {
      const permissionGroupsCollectionIdentifiers = permissionGroupsCollection.map(
        permissionGroupsItem => this.getPermissionGroupsIdentifier(permissionGroupsItem)!
      );
      const permissionGroupsToAdd = permissionGroups.filter(permissionGroupsItem => {
        const permissionGroupsIdentifier = this.getPermissionGroupsIdentifier(permissionGroupsItem);
        if (permissionGroupsCollectionIdentifiers.includes(permissionGroupsIdentifier)) {
          return false;
        }
        permissionGroupsCollectionIdentifiers.push(permissionGroupsIdentifier);
        return true;
      });
      return [...permissionGroupsToAdd, ...permissionGroupsCollection];
    }
    return permissionGroupsCollection;
  }
}
