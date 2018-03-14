using System.Collections.Generic;
using ConsoleApplication1.domain;
using MySql.Data.MySqlClient;

namespace ConsoleApplication1.repository
{
    public class DbUserRepository : AbstractDbRepository<User, int>
    {
        public DbUserRepository(string database, string table) : base(database, table)
        {
            ReadData();   
        }
        
        private void ReadData()
        {
            if (_db.Connect())
            {
                MySqlCommand cmd = new MySqlCommand();
                cmd.Connection = _db.Connection;
                cmd.CommandText = "select * from " + _table;
                cmd.Prepare();
           
                var rdr = cmd.ExecuteReader();
                lst.Clear();
                while (rdr.Read())
                {
                    lst.Add(rdr.GetInt32(0),
                        new User(rdr.GetInt32(0), rdr.GetString(1), rdr.GetInt32(2)));
                }
                _db.Close();
            }
        }
        
        public override User Save(User t)
        {
            if (_db.Connect())
            {
                MySqlCommand cmd = new MySqlCommand();
                cmd.Connection = _db.Connection;
                cmd.CommandText = "insert into " + _table + " (id, username, age) values (@id, @username, @age)";
                cmd.Prepare();

                cmd.Parameters.AddWithValue("@username", t.Username);
                cmd.Parameters.AddWithValue("@age", t.Age);
                cmd.Parameters.AddWithValue("@id", t.Id);
                cmd.ExecuteNonQuery();
                _db.Close();
            }
            
            return t;
        }

        public override bool Delete(int id)
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
            
            return base.Delete(id);
        }
        
        public override User Update(User t)
        {
            if (_db.Connect())
            {
                MySqlCommand cmd = new MySqlCommand();
                cmd.Connection = _db.Connection;
                cmd.CommandText = "update " + _table + " set username=@username, age=@age where id=@id";
                cmd.Prepare();

                cmd.Parameters.AddWithValue("@id", t.Id);
                cmd.Parameters.AddWithValue("@username", t.Username);
                cmd.Parameters.AddWithValue("@age", t.Age);
                cmd.ExecuteNonQuery();
                _db.Close();
            }
            
            return base.Update(t);
        }

        public override Dictionary<int, User> GetAll()
        {
            ReadData();
            return lst;
        }
    }
}