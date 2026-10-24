import React from 'react';
import { Routes, Route, Link, Navigate } from 'react-router-dom';
import { Box, Flex, Heading, Spacer, Button } from '@chakra-ui/react';
import ProductList from './pages/ProductList.jsx';
import ProductDetail from './pages/ProductDetail.jsx';
import SellerDashboard from './pages/SellerDashboard.jsx';

function App() {
  // Simple selector to switch between buyer and a mocked seller (ID 1)
  const [sellerId, setSellerId] = React.useState(null);

  const handleLoginAsSeller = (id) => {
    setSellerId(id);
  };

  const handleLogout = () => {
    setSellerId(null);
  };

  return (
    <Box>
      <Flex bg="gray.100" p={4} align="center">
        <Heading size="md">BajriX Marketplace</Heading>
        <Spacer />
        {sellerId ? (
          <>
            <Button mr={2} onClick={handleLogout}>Buyer View</Button>
            <Button as={Link} to="/seller" colorScheme="teal">
              Seller Dashboard
            </Button>
          </>
        ) : (
          <Button onClick={() => handleLoginAsSeller(1)} colorScheme="teal">
            Login as Seller (ID 1)
          </Button>
        )}
      </Flex>
      <Box p={4}>
        <Routes>
          <Route path="/" element={<ProductList />} />
          <Route path="/products/:id" element={<ProductDetail />} />
          <Route
            path="/seller"
            element={sellerId ? <SellerDashboard sellerId={sellerId} /> : <Navigate to="/" />}
          />
        </Routes>
      </Box>
    </Box>
  );
}

export default App;
