import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { Input, Box, Table, Thead, Tbody, Tr, Th, Td, Spinner, Stack, Button } from '@chakra-ui/react';
import axios from 'axios';

function ProductList() {
  const [search, setSearch] = useState('');
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(false);
  const [page, setPage] = useState(0);
  const size = 10;

  const fetch = async () => {
    setLoading(true);
    try {
      const res = await axios.get('http://localhost:8080/api/products', {
        params: { query: search, page, size },
      });
      setProducts(res.data.content);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetch();
  }, [search, page]);

  return (
    <Box>
      <Stack direction="row" mb={4}>
        <Input
          placeholder="Search products..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
        <Button onClick={() => setPage(0)}>Search</Button>
      </Stack>
      {loading ? (
        <Spinner />
      ) : (
        <Table variant="simple">
          <Thead>
            <Tr>
              <Th>Name</Th>
              <Th>Category</Th>
              <Th>Details</Th>
            </Tr>
          </Thead>
          <Tbody>
            {products.map((p) => (
              <Tr key={p.id}>
                <Td>{p.name}</Td>
                <Td>{p.category}</Td>
                <Td>
                  <Link to={`/products/${p.id}`}>View Offers</Link>
                </Td>
              </Tr>
            ))}
          </Tbody>
        </Table>
      )}
    </Box>
  );
}

export default ProductList;
