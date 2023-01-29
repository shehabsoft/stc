import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IItem } from '../item.model';
import { ItemService } from '../service/item.service';

@Injectable({ providedIn: 'root' })
export class ItemRoutingResolveService implements Resolve<IItem | null> {
  constructor(protected service: ItemService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IItem | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((item: HttpResponse<IItem>) => {
          if (item.body) {
            return of(item.body);
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
