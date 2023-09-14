import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { AcessoModulo } from "../_models/acessoModulo";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { AcessoFuncionalidade } from "../_models/acessoFuncionalidade";
import { map } from "rxjs/operators";
import { BaseService } from "./base.service";
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

    getPermissao(idModulo: string, idFuncionalidade: string) : Observable<PerfilFuncionalidade[]>{

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
