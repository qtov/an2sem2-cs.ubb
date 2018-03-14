using System;
using MySql.Data.MySqlClient;

namespace ConsoleApplication1.repository
{
    public class DbConnection
    {
        private DbConnection()
        {
        }

        private string _databaseName = string.Empty;
        public string DatabaseName
        {
            get { return _databaseName; }
            set { _databaseName = value; }
        }

        public string Password { get; set; }
        
        private MySqlConnection _connection = null;
        public MySqlConnection Connection
        {
            get { return _connection; }
        }

        private static DbConnection _instance = null;
        public static DbConnection Instance()
        {
            if (_instance == null)
                _instance = new DbConnection();
            return _instance;
        }

        public bool Connect()
        {
            if (Connection == null)
            {
                if (String.IsNullOrEmpty(_databaseName))
                    return false;
                string connstring = string.Format("Server=localhost; database={0}; UID=root; password=toor; SslMode=none", _databaseName);
                _connection = new MySqlConnection(connstring);
            }
            _connection.Open();
            
            return true;
        }

        public void Close()
        {
            _connection.Close();
        }        
    }
}