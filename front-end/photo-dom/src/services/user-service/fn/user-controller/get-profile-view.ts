/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { UserResponseProfileView } from '../../models/user-response-profile-view';

export interface GetProfileView$Params {
  id: number;
}

export function getProfileView(http: HttpClient, rootUrl: string, params: GetProfileView$Params, context?: HttpContext): Observable<StrictHttpResponse<UserResponseProfileView>> {
  const rb = new RequestBuilder(rootUrl, getProfileView.PATH, 'get');
  if (params) {
    rb.query('id', params.id, {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: '*/*', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<UserResponseProfileView>;
    })
  );
}

getProfileView.PATH = '/api/users/profile/view';
