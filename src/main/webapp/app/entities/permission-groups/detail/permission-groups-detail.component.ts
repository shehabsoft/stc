import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPermissionGroups } from '../permission-groups.model';

@Component({
  selector: 'jhi-permission-groups-detail',
  templateUrl: './permission-groups-detail.component.html',
})
export class PermissionGroupsDetailComponent implements OnInit {
  permissionGroups: IPermissionGroups | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ permissionGroups }) => {
      this.permissionGroups = permissionGroups;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
