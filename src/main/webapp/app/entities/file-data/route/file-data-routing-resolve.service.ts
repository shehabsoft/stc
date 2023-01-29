import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFileData } from '../file-data.model';
import { FileDataService } from '../service/file-data.service';

@Injectable({ providedIn: 'root' })
export class FileDataRoutingResolveService implements Resolve<IFileData | null> {
  constructor(protected service: FileDataService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFileData | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((fileData: HttpResponse<IFileData>) => {
          if (fileData.body) {
            return of(fileData.body);
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
