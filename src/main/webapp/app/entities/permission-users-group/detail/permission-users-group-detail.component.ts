import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPermissionUsersGroup } from '../permission-users-group.model';

@Component({
  selector: 'jhi-permission-users-group-detail',
  templateUrl: './permission-users-group-detail.component.html',
})
export class PermissionUsersGroupDetailComponent implements OnInit {
  permissionUsersGroup: IPermissionUsersGroup | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ permissionUsersGroup }) => {
      this.permissionUsersGroup = permissionUsersGroup;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
