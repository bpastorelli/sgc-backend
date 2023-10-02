import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { AcessoFuncionalidadeService } from "../acessos-funcionalidades/acessos-funcionalidades.service";
import { PerfilFuncionalidade } from "../acessos-funcionalidades/acesso-funcionalidade.model";
import { AcessoFuncionalidadeFilter } from "../acessos-funcionalidades/acesso-funcionalidade-filter.model";

@Injectable({ providedIn: 'root' })
export class PermissoesService  {

    private filter = {} as AcessoFuncionalidadeFilter;

    constructor(
        private http: HttpClient,
        private acesso: AcessoFuncionalidadeService
    ) 
    { }

    getPermissao(idModulo: string[], idFuncionalidade: string[]) : Observable<PerfilFuncionalidade[]>{

        this.filter.idModulo = idModulo;
        this.filter.idFuncionalidade = idFuncionalidade;
        this.filter.idUsuario = JSON.parse(localStorage.getItem('idUsuario'));
        this.filter.content = true;
        this.filter.acesso = true;
        this.filter.page = 0;
        this.filter.size = 10;

        return this.acesso.getAcessosFuncionalidadeUsuario(this.filter)


    }

}