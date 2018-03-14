using System;
using System.Data.Common;
using ConsoleApplication1.domain;
using MySql.Data.MySqlClient;

namespace ConsoleApplication1.repository
{
    public class AbstractDbRepository<T, ID> : AbstractRepository<T, ID> where T : IHasId<ID>
    {
        protected DbConnection _db;
        protected string Dbname;
        protected string _table;
            
        public AbstractDbRepository(string database, string table)
        {
            _db = DbConnection.Instance();
            _db.DatabaseName = database;
            Dbname = database;
            _table = table;
        }
        
        public override bool Delete(ID id)
        {
            if (_db.Connect())
            {
                MySqlCommand cmd = new MySqlCommand();
                cmd.Connection = _db.Connection;
                cmd.CommandText = "delete from " + _table + " where id=@id";
                cmd.Prepare();

                cmd.Parameters.AddWithValue("@id", id);
                cmd.ExecuteNonQuery();
                _db.Close();
            }
            
            return lst.Remove(id);
        }
    }
}