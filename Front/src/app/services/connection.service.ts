import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AdjacencyListItem } from '../AdjacencyListItem';
import { Data } from './Data';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class ConnectionService {
	private apiUrl = 'http://localhost:8080/';
  	constructor(private http: HttpClient) { }

	sendLists(edgeList: AdjacencyListItem[], nodeList:String[]) {
		const headers = { 'content-type': 'application/json'}
		const body=JSON.stringify(this.getData(edgeList, nodeList));
		console.log(body)
		return this.http.post(this.apiUrl + 'input', body,{'headers':headers})
	}

  sendClear() {
    return this.http.delete(this.apiUrl + 'clear')
  }

  requestOutput() {
      return this.http.get(this.apiUrl + 'output');
  }

  requestPaths():Observable<String> {
	return this.http.get(this.apiUrl + 'paths', {responseType:"text"});
}

requestLoops():Observable<String> {
	return this.http.get(this.apiUrl + 'loops', {responseType:"text"});
}

requestIndividualLoops():Observable<String> {
	return this.http.get(this.apiUrl + 'individualLoops', {responseType:"text"});
}

requestDelta():Observable<String> {
	return this.http.get(this.apiUrl + 'deltas', {responseType:"text"});
}

	getData (edgeList: AdjacencyListItem[], nodeList:String[]):Data {
		var sources:String[] = []
		var destinations:String[] = []
		var gains:number[] = []

		for (let i = 0; i < edgeList.length; i++){
			sources.push(edgeList[i].src)
			destinations.push(edgeList[i].dest)
			gains.push(edgeList[i].gain)
		}

		var dataToBeSend:Data = {sources:sources, destinations:destinations, gains:gains, nodes: nodeList}
		return dataToBeSend
	}

}
function responseType<T>(arg0: string, responseType: any, arg2: string) {
	throw new Error('Function not implemented.');
}

