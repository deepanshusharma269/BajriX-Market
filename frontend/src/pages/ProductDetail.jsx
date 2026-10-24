import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { Box, Heading, Text, Table, Thead, Tbody, Tr, Th, Td, Spinner, Badge } from '@chakra-ui/react';
import axios from 'axios';

function ProductDetail() {
  const { id } = useParams();
  const [product, setProduct] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetch = async () => {
      try {
        const res = await axios.get(`http://localhost:8080/api/products/${id}`);
        setProduct(res.data);
      } catch (e) {
        console.error(e);
      } finally {
        setLoading(false);
      }
    };
    fetch();
  }, [id]);

  if (loading) return <Spinner />;
  if (!product) return <Text>Product not found.</Text>;

  return (
    <Box>
      <Heading mb={4}>{product.name}</Heading>
      <Text mb={2}>Category: {product.category || 'N/A'}</Text>
      <Text mb={4}>{product.description}</Text>
      <Heading size="md" mb={2}>Available Offers</Heading>
      {product.listings.length === 0 ? (
        <Text>No active listings.</Text>
      ) : (
        <Table variant="simple">
          <Thead>
            <Tr>
              <Th>Seller</Th>
              <Th>Price (₹)</Th>
              <Th>Stock</Th>
              <Th>Min Order</Th>
            </Tr>
          </Thead>
          <Tbody>
            {product.listings.map((l) => (
              <Tr key={l.id}>
                <Td>{l.sellerName}</Td>
                <Td>{l.price.toFixed(2)}</Td>
                <Td>{l.stock}</Td>
                <Td>{l.minOrderQty}</Td>
              </Tr>
            ))}
          </Tbody>
        </Table>
      )}
    </Box>
  );
}

export default ProductDetail;
