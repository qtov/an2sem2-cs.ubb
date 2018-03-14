namespace ConsoleApplication1.repository
{
    public interface IHasId<ID>
    {
        ID Id { get; set; }
    }
}