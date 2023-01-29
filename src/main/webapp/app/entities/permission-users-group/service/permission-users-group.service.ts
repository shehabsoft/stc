import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPermissionUsersGroup, NewPermissionUsersGroup } from '../permission-users-group.model';

export type PartialUpdatePermissionUsersGroup = Partial<IPermissionUsersGroup> & Pick<IPermissionUsersGroup, 'id'>;

export type EntityResponseType = HttpResponse<IPermissionUsersGroup>;
export type EntityArrayResponseType = HttpResponse<IPermissionUsersGroup[]>;

@Injectable({ providedIn: 'root' })
export class PermissionUsersGroupService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/permission-users-groups');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(permissionUsersGroup: NewPermissionUsersGroup): Observable<EntityResponseType> {
    return this.http.post<IPermissionUsersGroup>(this.resourceUrl, permissionUsersGroup, { observe: 'response' });
  }

  update(permissionUsersGroup: IPermissionUsersGroup): Observable<EntityResponseType> {
    return this.http.put<IPermissionUsersGroup>(
      `${this.resourceUrl}/${this.getPermissionUsersGroupIdentifier(permissionUsersGroup)}`,
      permissionUsersGroup,
      { observe: 'response' }
    );
  }

  partialUpdate(permissionUsersGroup: PartialUpdatePermissionUsersGroup): Observable<EntityResponseType> {
    return this.http.patch<IPermissionUsersGroup>(
      `${this.resourceUrl}/${this.getPermissionUsersGroupIdentifier(permissionUsersGroup)}`,
      permissionUsersGroup,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPermissionUsersGroup>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPermissionUsersGroup[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPermissionUsersGroupIdentifier(permissionUsersGroup: Pick<IPermissionUsersGroup, 'id'>): number {
    return permissionUsersGroup.id;
  }

  comparePermissionUsersGroup(o1: Pick<IPermissionUsersGroup, 'id'> | null, o2: Pick<IPermissionUsersGroup, 'id'> | null): boolean {
    return o1 && o2 ? this.getPermissionUsersGroupIdentifier(o1) === this.getPermissionUsersGroupIdentifier(o2) : o1 === o2;
  }

  addPermissionUsersGroupToCollectionIfMissing<Type extends Pick<IPermissionUsersGroup, 'id'>>(
    permissionUsersGroupCollection: Type[],
    ...permissionUsersGroupsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const permissionUsersGroups: Type[] = permissionUsersGroupsToCheck.filter(isPresent);
    if (permissionUsersGroups.length > 0) {
      const permissionUsersGroupCollectionIdentifiers = permissionUsersGroupCollection.map(
        permissionUsersGroupItem => this.getPermissionUsersGroupIdentifier(permissionUsersGroupItem)!
      );
      const permissionUsersGroupsToAdd = permissionUsersGroups.filter(permissionUsersGroupItem => {
        const permissionUsersGroupIdentifier = this.getPermissionUsersGroupIdentifier(permissionUsersGroupItem);
        if (permissionUsersGroupCollectionIdentifiers.includes(permissionUsersGroupIdentifier)) {
          return false;
        }
        permissionUsersGroupCollectionIdentifiers.push(permissionUsersGroupIdentifier);
        return true;
      });
      return [...permissionUsersGroupsToAdd, ...permissionUsersGroupCollection];
    }
    return permissionUsersGroupCollection;
  }
}
