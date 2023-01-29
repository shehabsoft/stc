import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPermissionGroups } from '../permission-groups.model';
import { PermissionGroupsService } from '../service/permission-groups.service';

@Injectable({ providedIn: 'root' })
export class PermissionGroupsRoutingResolveService implements Resolve<IPermissionGroups | null> {
  constructor(protected service: PermissionGroupsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPermissionGroups | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((permissionGroups: HttpResponse<IPermissionGroups>) => {
          if (permissionGroups.body) {
            return of(permissionGroups.body);
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
