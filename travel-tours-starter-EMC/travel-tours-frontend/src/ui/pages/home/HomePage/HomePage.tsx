import { Box, Container, Typography } from '@mui/material';

const HomePage = () => {
  return (
    <Box sx={{ m: 0, p: 0 }} className='welcome-section'>
      <Container maxWidth='xl' sx={{ mt: 3, py: 3 }}>
        <Typography variant='h4' gutterBottom>
          Welcome to Travel Tours App! 👋
        </Typography>
        <Typography variant='body1' sx={{ mb: 4 }}>
          This is the home page.
        </Typography>
      </Container>
    </Box>

  );
};

export default HomePage;