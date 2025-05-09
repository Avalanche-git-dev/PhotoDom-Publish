/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { PhotoResponseString } from '../../models/photo-response-string';

export interface UploadPhoto$Params {
      body?: {
'file': Blob;
}
}

export function uploadPhoto(http: HttpClient, rootUrl: string, params?: UploadPhoto$Params, context?: HttpContext): Observable<StrictHttpResponse<PhotoResponseString>> {
  const rb = new RequestBuilder(rootUrl, uploadPhoto.PATH, 'post');
  if (params) {
    rb.body(params.body, 'multipart/form-data');
  }

  return http.request(
    rb.build({ responseType: 'json', accept: '*/*', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<PhotoResponseString>;
    })
  );
}

uploadPhoto.PATH = '/api/photos/upload';
