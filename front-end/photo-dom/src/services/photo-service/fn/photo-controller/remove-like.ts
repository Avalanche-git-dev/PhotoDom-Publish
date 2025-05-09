/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { PhotoResponseVoid } from '../../models/photo-response-void';

export interface RemoveLike$Params {
  photoId: number;
}

export function removeLike(http: HttpClient, rootUrl: string, params: RemoveLike$Params, context?: HttpContext): Observable<StrictHttpResponse<PhotoResponseVoid>> {
  const rb = new RequestBuilder(rootUrl, removeLike.PATH, 'delete');
  if (params) {
    rb.query('photoId', params.photoId, {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: '*/*', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<PhotoResponseVoid>;
    })
  );
}

removeLike.PATH = '/api/photos/like/remove';
