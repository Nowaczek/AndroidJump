//------------------------------------------------------------------------------
// <auto-generated>
//    This code was generated from a template.
//
//    Manual changes to this file may cause unexpected behavior in your application.
//    Manual changes to this file will be overwritten if the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

namespace MvcApplication1
{
    using System;
    using System.Collections.Generic;
    
    public partial class Mandaty
    {
        public int Id_Mandatu { get; set; }
        public int Id_Klienta { get; set; }
        public int Id_Kontrolera { get; set; }
        public decimal Kwota { get; set; }
        public System.DateTime Data_wystawienia { get; set; }
    
        public virtual Klienci Klienci { get; set; }
        public virtual Kontrolerzy Kontrolerzy { get; set; }
    }
}
