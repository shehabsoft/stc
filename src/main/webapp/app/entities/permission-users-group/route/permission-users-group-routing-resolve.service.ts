import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPermissionUsersGroup } from '../permission-users-group.model';
import { PermissionUsersGroupService } from '../service/permission-users-group.service';

@Injectable({ providedIn: 'root' })
export class PermissionUsersGroupRoutingResolveService implements Resolve<IPermissionUsersGroup | null> {
  constructor(protected service: PermissionUsersGroupService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPermissionUsersGroup | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((permissionUsersGroup: HttpResponse<IPermissionUsersGroup>) => {
          if (permissionUsersGroup.body) {
            return of(permissionUsersGroup.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
