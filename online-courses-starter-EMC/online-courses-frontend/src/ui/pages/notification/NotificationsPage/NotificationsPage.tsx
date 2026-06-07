import { Box, CircularProgress, Typography } from '@mui/material';
import useNotifications from '../../../../hooks/useNotifications.ts';
import NotificationsList from '../../../components/notification/NotificationsList/NotificationsList.tsx';

const NotificationsPage = () => {
  const { notifications, loading } = useNotifications();

  return (
    <Box className='notifications-box' sx={{ mt: 2 }}>
      <Typography variant='h5' sx={{ mb: 2 }}>
        Notifications
      </Typography>
      {loading && (
        <Box className='progress-box'>
          <CircularProgress/>
        </Box>
      )}
      {!loading && <NotificationsList notifications={notifications}/>}
    </Box>
  );
};

export default NotificationsPage;
