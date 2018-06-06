import { Injectable } from '@angular/core';
import { Http, Response, ResponseContentType } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { Headers, RequestOptions,RequestMethod } from '@angular/http';
import * as Rx from "rxjs/Rx"
import { RouterModule, Router } from '@angular/router';
import { Jsonp, URLSearchParams } from '@angular/http'; 
import { HttpClient,HttpRequest,HttpEvent} from '@angular/common/http';
import 'rxjs/add/operator/map';
import { forEach } from '@angular/router/src/utils/collection';
import { catchError, map, tap } from 'rxjs/operators';
import {  HttpHeaders } from "@angular/common/http";
@Injectable()
export class sharingPortalService {
    
    constructor(private http: HttpClient) { }  
    private url = 'http://localhost:8080/listAll';
    private formdata: FormData = new FormData();
    
    /* This service call is for listAll files */
    onGoToDownload(){
       let Response = this.http.get(this.url)
              .map(response => response)
              console.log('Response is UtilityService'+Response);      
        return Response;
    }
   
   /* This call is for fetching particular file */
    searchByName(fileId: string){
        let urls = 'http://localhost:8080/searchId/';  
        urls=urls+fileId;
        console.log("urls"+urls);
        let Response = this.http.get(urls)
        .map(response => response)
        console.log("response from serachByName"+Response);
            return Response;
            
}

/* This call is for insert file,name and description */
uploadFile(params:any,file: File){
   
     let urls = 'http://localhost:8080/uploadfile?anthemId='+params.Uid+'&email='+params.email+'&filename='+params.filename+'&category='+params.category+'&fileDesc='+params.description+'&readMe='+params.readme;    //let urls = 'http://localhost:8080/uploadfile';
    this.formdata.append('file', file);
    this.formdata.append('sharingPortalObj', params);
    console.log("params"+JSON.stringify(params));
    const req = new HttpRequest('POST',urls , this.formdata, {
        reportProgress: true,
        responseType: 'text',
       
      });
      return  this.http.request(req);
        
   
}


/* This call is for download file */
setMultipartHeader(headers:Headers){
    headers.append('method', 'GET');
    
    }
    
getMultipart(fileId){
    var headers = new Headers();
    this.setMultipartHeader(headers);
     let requestOptions = new RequestOptions({
    headers: headers,
    responseType:ResponseContentType.Blob,//dont forget to import the enum
    
    });
    
    let urls = 'http://localhost:8080/download/'; 
    urls=urls+fileId;
    let Response = this.http.get(urls,{
        headers: new HttpHeaders().set('Content-Type', 'application/zip'),
        responseType: 'arraybuffer'
     })
    .map(response => response)
    console.log("response from serachByName"+Response);
    

    return Response;
}

/* This service call is for getting top three downloaded files */
    getTopThree(){
        let urltopThree = 'http://localhost:8080/topThree/'; 
       let Response = this.http.get(urltopThree)
              .map(response => response)
              console.log('Response top 3->'+JSON.stringify(Response));      
        return Response;
    }
   
}
