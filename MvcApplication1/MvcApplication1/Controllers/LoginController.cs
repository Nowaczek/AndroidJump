using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web;
using System.Web.Http;
using System.Web.Mvc;
using System.Web.UI.WebControls;
using WebGrease.Css.Ast.Selectors;

namespace MvcApplication1.Controllers
{   
    public class LoginController : ApiController
    {
        public HttpResponseMessage Login(login person)
        {
            if (!this.ModelState.IsValid)
            {
                //tutaj wywala ogolny blad gdy otrzymal zle dane
                throw new HttpResponseException(Request.CreateResponse(HttpStatusCode.BadRequest));
            }

            loginAnswer reply = new loginAnswer{ok = 0,tocken = null};

            if (Zapytania.czy_poprawne_dane_logowania_uzytkownia(person.Login, person.Password) == true)
            {
                reply.ok = 0;
                reply.tocken = Token.generuj_token();
                
                if (Zapytania.zapisz_token(person.Login, reply.tocken) == false)
                {
                    reply.ok = 1;
                    reply.tocken = "blad";
                }
            }
            else
            {
                reply.ok = 1;
                reply.tocken = "blad";
            }
            
            HttpResponseMessage response = this.Request.CreateResponse(HttpStatusCode.Created, reply);            

            return response;
        }
    }//class
}